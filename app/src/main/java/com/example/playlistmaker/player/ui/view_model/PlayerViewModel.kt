package com.example.playlistmaker.player.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.library.domain.api.FavoritesInteractor
import com.example.playlistmaker.library.domain.api.PlaylistInteractor
import com.example.playlistmaker.library.domain.models.Playlist
import com.example.playlistmaker.player.domain.api.PlayerInteractor
import com.example.playlistmaker.player.domain.models.MediaState
import com.example.playlistmaker.search.domain.models.Track
import com.google.gson.Gson
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerViewModel(
    private val playerInteractor: PlayerInteractor,
    private val favoritesInteractor: FavoritesInteractor,
    private val playlistInteractor: PlaylistInteractor
) : ViewModel() {

    private companion object {
        const val DELAY = 300L
        const val TIME_FORMAT = "mm:ss"
    }

    private var timerJob: Job? = null

    private val track: Track? = playerInteractor.loadTrack()
    private val previewUrl: String = track?.previewUrl ?: String()

    private val _currentPosition = MutableLiveData<String>()
    val currentPosition: LiveData<String> get() = _currentPosition

    private val _mediaState = MutableLiveData<MediaState>()
    val mediaState: LiveData<MediaState> get() = _mediaState

    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean> get() = _isFavorite

    private val _playlists = MutableLiveData<List<Playlist>>()
    val playlists: LiveData<List<Playlist>> get() = _playlists

    val timePlay: String = SimpleDateFormat(
        TIME_FORMAT,
        Locale.getDefault()
    ).format(track?.trackTimeMillis?.toLong() ?: String())


    init {
        playerInteractor.observeMediaState(_mediaState::postValue)
        preparePlayer()
        getFavorites(track)
        getPlaylists()
    }

    override fun onCleared() {
        releasePlayer()
    }

    fun getCoverUrl() = track?.artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg")

    fun onClickPlayButton() {
        when (mediaState.value) {
            MediaState.Default -> { /* NOP*/ }

            MediaState.Paused -> play()
            MediaState.Playing -> pause()
            is MediaState.Prepared -> play()

            null -> { /* NOP*/ }
        }
    }

    fun getTrack(): Track? = track

    fun pause() {
        if (previewUrl.isNotEmpty()) {
            playerInteractor.pause()
        }
    }

    fun releasePlayer() {
        if (previewUrl.isNotEmpty()) {
            playerInteractor.releasePlayer()
        }
    }

    fun addTrackToPlaylist(playlist: Playlist): Int {
        var resultCode = 0
        val trackList: MutableList<String> = if (playlist.tracks.isEmpty()) {
            mutableListOf()
        } else convertStringToList(playlist.tracks).toMutableList()

        track?.let { track ->

            if (trackList.contains(track.trackId)) {
                resultCode = 1
                return resultCode
            }

            trackList.add(track.trackId)

            viewModelScope.launch {
                try {
                    playlistInteractor.addTracksToPlaylist(playlist.plID, trackList, trackList.count())
                } catch (_: Exception) {
                    resultCode = 2
                }
            }
        } ?: run {
            resultCode = 3
        }
        return resultCode
    }

    suspend fun toggleFavorite() {
        if (track != null) {
            val trackContains = favoritesInteractor.getTracksID()
                .firstOrNull { items -> items.contains(track.trackId) } != null

            if (trackContains) {
                favoritesInteractor.removeTrack(track)
            } else {
                favoritesInteractor.addTrack(track)
            }
            getFavorites(track)
        }
    }

    private fun preparePlayer() {
        playerInteractor.preparePlayer()
    }

    private fun play() {
        if (previewUrl.isNotEmpty()) {
            playerInteractor.play()
            startUpdatingTime()
        }
    }

    private fun startUpdatingTime() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (playerInteractor.isPlaying()) {
                _currentPosition.postValue(getPositionToString(playerInteractor.currentPosition()))
                delay(DELAY)
            }
        }
    }

    private fun getPositionToString(position: Int): String {
        return SimpleDateFormat(
            TIME_FORMAT,
            Locale.getDefault()
        ).format(position)
    }

    private suspend fun isFavorite(track: Track) {
        val isFavorite = favoritesInteractor.getTracksID()
            .firstOrNull { items -> items.contains(track.trackId) } != null

        _isFavorite.postValue(isFavorite)
    }

    private fun getFavorites(track: Track?) {
        if (track != null) {
            viewModelScope.launch {
                isFavorite(track)
            }
        }
    }

    private fun getPlaylists() {
        viewModelScope.launch {
            playlistInteractor.getAllPlaylists()
                .collect { playlists ->
                    _playlists.postValue(playlists)
                }
        }
    }

    private fun convertStringToList(jsonString: String): List<String> {
        val gson = Gson()
        return gson.fromJson(jsonString, Array<String>::class.java).toList()
    }

}