package com.example.playlistmaker

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlin.collections.ArrayList

const val SEARCH_HISTORY_KEY = "searchHistory"

class SearchHistory(val sharedPref: SharedPreferences) {

    private var history = ArrayList<Track>()

    private fun saveHistory(){
        val editor = sharedPref.edit()
        editor.putString(SEARCH_HISTORY_KEY, Gson().toJson(history))
        editor.apply()
    }

    fun getHistory(): ArrayList<Track> {
        if (history.isEmpty()){
            history = loadHistory()
            history.reverse()
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
        if (!history.isEmpty()){
            history.clear()
        }
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