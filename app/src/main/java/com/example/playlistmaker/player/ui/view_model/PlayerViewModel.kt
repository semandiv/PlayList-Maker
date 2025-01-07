package com.example.playlistmaker.player.ui.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.library.domain.api.FavoritesInteractor
import com.example.playlistmaker.library.domain.api.PlaylistInteractor
import com.example.playlistmaker.library.domain.models.Playlist
import com.example.playlistmaker.player.domain.api.PlayerInteractor
import com.example.playlistmaker.player.domain.models.MediaState
import com.example.playlistmaker.player.domain.models.PlaylistAddResult
import com.example.playlistmaker.search.domain.models.Track
import com.google.gson.Gson
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerViewModel(
    private val playerInteractor: PlayerInteractor,
    private val favoritesInteractor: FavoritesInteractor,
    private val playlistInteractor: PlaylistInteractor,
    private val gson: Gson
) : ViewModel() {

    private companion object {
        const val DELAY = 300L
        const val TIME_FORMAT = "mm:ss"
        const val DEFAULT_TIME = "00:00"
    }

    private var timerJob: Job? = null

    private val track: Track? = playerInteractor.loadTrack()
    private val previewUrl: String = track?.previewUrl ?: String()

    private val _currentPosition = MutableStateFlow(DEFAULT_TIME)
    val currentPosition: StateFlow<String> get() = _currentPosition

    private val _mediaState = MutableStateFlow<MediaState>(MediaState.Default)
    val mediaState: StateFlow<MediaState> get() = _mediaState

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> get() = _isFavorite

    private val _playlists = MutableStateFlow<List<Playlist>>(emptyList())
    val playlists: StateFlow<List<Playlist>> get() = _playlists

    val timePlay: String = SimpleDateFormat(
        TIME_FORMAT,
        Locale.getDefault()
    ).format(track?.trackTimeMillis?.toLong() ?: String())


    init {
        playerInteractor.observeMediaState{ newState ->
            _mediaState.value = newState}
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

        }
    }

    fun getTrack(): Track? = track

    fun updatePlayer() {
        playerInteractor.updatePlayerState()
    }

    fun pause() {
        if (previewUrl.isNotEmpty()) {
            playerInteractor.pause()
        }
    }

    fun addTrackToPlaylist(playlist: Playlist): PlaylistAddResult {
        val plName = playlist.plName
        val trackList: MutableList<String> = if (playlist.tracks.isEmpty()) {
            mutableListOf()
        } else convertStringToList(playlist.tracks).toMutableList()

        val currentTrack = track

        return if (currentTrack != null) {
            if (trackList.contains(currentTrack.trackId)) {
                PlaylistAddResult.AlreadyExists(plName)
            } else {
                trackList.add(currentTrack.trackId)
                try {
                    viewModelScope.launch {
                        playlistInteractor.addTracksToPlaylist(playlist.plID, trackList, trackList.count())
                    }
                    PlaylistAddResult.Success(plName)
                } catch (e: Exception) {
                    PlaylistAddResult.Error
                }
            }
        } else {
            PlaylistAddResult.TrackIdNotFound
        }
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
                _currentPosition.value = getPositionToString(playerInteractor.currentPosition())
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

        _isFavorite.value = isFavorite
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
                    _playlists.value = playlists
                }
        }
    }

    private fun convertStringToList(jsonString: String): List<String> {
        return gson.fromJson(jsonString, Array<String>::class.java).toList()
    }

    private fun releasePlayer() {
        if (previewUrl.isNotEmpty()) {
            playerInteractor.releasePlayer()
        }
    }

}