package com.example.playlistmaker.search.data

import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.playlistmaker.search.domain.api.TrackPlayRepository
import com.example.playlistmaker.search.domain.models.Track
import com.google.gson.Gson

class TrackPlayRepositoryImpl(private val sharedPref: SharedPreferences, private val gson: Gson): TrackPlayRepository {
    private companion object{
        const val SAVED_TRACK_TO_PLAY= "saved_track_to_play"
    }

    override fun setTrackToPlay(track: Track) {
        sharedPref.edit().clear().apply()
        sharedPref.edit {
            putString(SAVED_TRACK_TO_PLAY, gson.toJson(track))
        }
    }
}