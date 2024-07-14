package com.example.playlistmaker

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

const val SEARCH_HISTORY_KEY = "searchHistory"
const val SEARCH_HISTORY_SIZE = 10

class SearchHistory(val sharedPref: SharedPreferences) {

    private var history = ArrayList<Track>()
    private val gson = Gson()

    init {
        history = loadHistory()
    }

    private fun saveHistory(){
        val editor = sharedPref.edit()
        editor.putString(SEARCH_HISTORY_KEY, gson.toJson(history))
        editor.apply()
    }

    fun getHistory(): ArrayList<Track> {
        if (history.isEmpty()){
            history = loadHistory()
        }
        val currentHistory = ArrayList<Track>()
        currentHistory.addAll(history)
        return currentHistory
    }

    private fun loadHistory(): ArrayList<Track> {
        val itemType = object : TypeToken<ArrayList<Track>>() {}.type
        val jsonString = sharedPref.getString(SEARCH_HISTORY_KEY, null)
        if (jsonString!= null){
            return gson.fromJson<ArrayList<Track>>(jsonString, itemType)
        } else {
            return ArrayList<Track>()
        }
    }

    fun clearHistory(){
        history.clear()
        saveHistory()
    }

    fun addHistory(newTrack: Track){
        if (history.size >= SEARCH_HISTORY_SIZE){
            history.removeAt(0)
        }

        history.removeIf {
            it.trackId == newTrack.trackId
        }

        history.add(newTrack)
        saveHistory()
    }
}