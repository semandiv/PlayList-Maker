package com.example.playlistmaker

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlin.collections.ArrayList

const val SEARCH_HISTORY_KEY = "searchHistory"

class SearchHistory(val sharedPref: SharedPreferences) {

    private var history = ArrayList<Track>()

    fun saveHistory(){
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

    fun loadHistory(): ArrayList<Track> {
        val itemType = object : TypeToken<ArrayList<Track>>() {}.type
        val jsonString = sharedPref.getString(SEARCH_HISTORY_KEY, null)
        if (jsonString!= null){
            return Gson().fromJson(jsonString, itemType)
        } else {
            return ArrayList()
        }
    }

    fun clearHistory(){
        if (!history.isEmpty()){
            history.clear()
        }
        saveHistory()
    }

    fun addHistory(track: Track){
        if (!history.contains(track)){
            history.add(track)
            saveHistory()
        } else {
            history.remove(track)
            history.add(track)
            saveHistory()
        }
    }
}