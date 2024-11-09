package com.example.playlistmaker.search.data

import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.playlistmaker.search.domain.api.SharedPrefRepository
import com.example.playlistmaker.search.domain.models.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SharedPrefRepositoryImpl(
    private val sharedPref: SharedPreferences,
    private val gson: Gson
) : SharedPrefRepository {
    private companion object {
        const val SEARCH_HISTORY_KEY = "searchHistory"
        const val SEARCH_HISTORY_SIZE = 10
    }

    private val history = mutableListOf<Track>()

    override fun getTrack(): List<Track> {
        if (history.isEmpty()) {
            history.addAll(loadHistory())
        }
        return history.toList()
    }

    override fun clearHistory() {
        sharedPref.edit().clear().apply()
        history.addAll(loadHistory())
    }

    override fun addTrack(track: Track) {
        (loadHistory() + listOf(track))
            .distinct()
            .sortedBy { it != track }
            .take(SEARCH_HISTORY_SIZE)
            .let { updateHistory(it) }
    }

    private fun updateHistory(it: List<Track>) {
        history.clear()
        history.addAll(it)
        saveTrack()
    }

    private fun saveTrack() {
        sharedPref.edit {
            putString(SEARCH_HISTORY_KEY, gson.toJson(history))
        }
    }

    private fun loadHistory(): List<Track> {
        val itemType = object : TypeToken<List<Track>>() {}.type
        return sharedPref
            .getString(SEARCH_HISTORY_KEY, null)
            ?.let { jsonString -> gson.fromJson(jsonString, itemType) }
            ?: emptyList()
    }
}