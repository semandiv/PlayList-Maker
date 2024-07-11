package com.example.playlistmaker

import android.view.View

interface TrackClickListener {
    fun onTrackClick(position: Int, view: View)
}