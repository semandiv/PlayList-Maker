package com.example.playlistmaker

import android.annotation.SuppressLint
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val trackName: TextView = itemView.findViewById(R.id.trackName)
    private val artistName: TextView = itemView.findViewById(R.id.artistName_trackTime)
    private val artwork: ImageView = itemView.findViewById(R.id.artwork)

    private val separator = " \u2022 "

    fun bind(track: Track) {
        trackName.text = track.trackName
        artistName.text = "${track.artistName}$separator${track.trackTime}"

        Glide.with(itemView.context)
            .load(track.artworkUrl100)
            .placeholder(R.drawable.baseline_album_24)
            .error(R.drawable.baseline_album_24)
            .transform(RoundedCorners(16))
            .into(artwork)
    }
}