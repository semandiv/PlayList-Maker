package com.example.playlistmaker.data

import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.playlistmaker.domain.api.SharedPrefRepository
import com.example.playlistmaker.domain.models.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SharedPrefRepositoryImpl(private val sharedPref: SharedPreferences) : SharedPrefRepository {
    private companion object {
        const val SEARCH_HISTORY_KEY = "searchHistory"
        const val SEARCH_HISTORY_SIZE = 10
    }

    private val history = mutableListOf<Track>()
    private val gson = Gson()

    init {
        history.addAll(loadHistory())
    }

    override fun getTrack(): MutableList<Track> {
        if (history.isEmpty()) {
            history.addAll(loadHistory())
        }
        val currentHistory = mutableListOf<Track>()
        currentHistory.addAll(history)
        return currentHistory
    }

    override fun clearTrack() {
        history.clear()
        saveTrack()
    }

    override fun addTrack(track: Track) {
        if (history.size >= SEARCH_HISTORY_SIZE) {
            history.removeAt(0)
        }

        history.removeIf {
            it.trackId == track.trackId
        }

        history.add(track)
        saveTrack()
    }

    private fun saveTrack() {
        sharedPref.edit {
            putString(SEARCH_HISTORY_KEY, gson.toJson(history))
        }
    }

    private fun loadHistory(): List<Track> {
        val itemType = object : TypeToken<MutableList<Track>>() {}.type
        val jsonString =
            sharedPref.getString(SEARCH_HISTORY_KEY, null)
        return if (jsonString != null) gson.fromJson<MutableList<Track>>(
            jsonString,
            itemType
        ) else mutableListOf<Track>()
    }
}