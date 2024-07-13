package com.example.playlistmaker

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

const val SEARCH_HISTORY_KEY = "searchHistory"

class SearchHistory(val sharedPref: SharedPreferences) {

    private var history = ArrayList<Track>()

    init {
        history = loadHistory()
    }

    private fun saveHistory(){
        val editor = sharedPref.edit()
        editor.putString(SEARCH_HISTORY_KEY, Gson().toJson(history))
        editor.apply()
    }

    fun getHistory(): ArrayList<Track> {
        if (history.isEmpty()){
            history = loadHistory()
        }
        return history
    }

    private fun loadHistory(): ArrayList<Track> {
        val itemType = object : TypeToken<ArrayList<Track>>() {}.type
        val jsonString = sharedPref.getString(SEARCH_HISTORY_KEY, null)
        if (jsonString!= null){
            return Gson().fromJson<ArrayList<Track>>(jsonString, itemType)
        } else {
            return ArrayList<Track>()
        }
    }

    fun clearHistory(){
        history.clear()
        saveHistory()
    }

    fun addHistory(newTrack: Track){
        if (history.size >= 10){
            history.removeAt(0)
        }

        history.removeIf {
            it.trackId == newTrack.trackId
        }

        history.add(newTrack)
        saveHistory()
    }
}