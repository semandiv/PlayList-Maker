package com.example.playlistmaker.data

import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.playlistmaker.domain.api.SharedPrefRepository
import com.example.playlistmaker.domain.models.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SharedPrefRepositoryImpl(private val sharedPref: SharedPreferences, private val gson: Gson) : SharedPrefRepository {
    private companion object {
        const val SEARCH_HISTORY_KEY = "searchHistory"
        const val SEARCH_HISTORY_SIZE = 10
    }

    private val history = mutableListOf<Track>()

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
        val tempHistory = mutableListOf(track).apply {
            addAll(history)
        }.distinctBy { it.trackId }
        history.clear()
        history.addAll(tempHistory.take(SEARCH_HISTORY_SIZE))
        saveTrack()
    }

    private fun saveTrack() {
        sharedPref.edit {
            putString(SEARCH_HISTORY_KEY, gson.toJson(history))
        }
    }

    private fun loadHistory(): List<Track> {
        val itemType = object : TypeToken<MutableList<Track>>() {}.type
        return sharedPref
            .getString(SEARCH_HISTORY_KEY, null)
            ?.let {jsonString -> gson.fromJson<MutableList<Track>>(jsonString, itemType)}
            ?:emptyList()
    }
}