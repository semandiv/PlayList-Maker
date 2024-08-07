package com.example.playlistmaker

import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

const val SEARCH_HISTORY_KEY = "searchHistory"
const val SEARCH_HISTORY_SIZE = 10

class SearchHistory(private val sharedPref: SharedPreferences) {

    private val history = mutableListOf<Track>()
    private val gson = Gson()

    init {
        history.addAll(loadHistory())
    }

    private fun saveHistory() {
        sharedPref.edit {
            putString(SEARCH_HISTORY_KEY, gson.toJson(history))
        }
    }

    fun getHistory(): List<Track> {
        if (history.isEmpty()) {
            history.addAll(loadHistory())
        }
        val currentHistory = mutableListOf<Track>()
        currentHistory.addAll(history)
        return currentHistory.toList()
    }

    private fun loadHistory(): List<Track> {
        val itemType = object : TypeToken<MutableList<Track>>() {}.type
        val jsonString = sharedPref.getString(SEARCH_HISTORY_KEY, null)
        return if (jsonString!= null) gson.fromJson<MutableList<Track>>(jsonString, itemType) else mutableListOf<Track>()
    }

    fun clearHistory() {
        history.clear()
        saveHistory()
    }

    fun addHistory(newTrack: Track) {
        if (history.size >= SEARCH_HISTORY_SIZE) {
            history.removeAt(0)
        }

        history.removeIf {
            it.trackId == newTrack.trackId
        }

        history.add(newTrack)
        saveHistory()
    }
}