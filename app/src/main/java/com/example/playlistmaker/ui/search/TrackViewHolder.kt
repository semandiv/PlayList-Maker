package com.example.playlistmaker.ui.search

import android.annotation.SuppressLint
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.models.Track
import java.text.SimpleDateFormat
import java.util.Locale

class TrackViewHolder(itemView: View, private val adapter: TracksAdapter) : RecyclerView.ViewHolder(itemView){
    private val trackName: TextView = itemView.findViewById(R.id.trackName)
    private val artistName: TextView = itemView.findViewById(R.id.artistName)
    private val artwork: ImageView = itemView.findViewById(R.id.artwork)
    private val trackTime: TextView = itemView.findViewById(R.id.trackTime)

    private val separator = " \u2022 "

    @SuppressLint("SetTextI18n")
    fun bind(track: Track) {
        trackName.text = track.trackName
        artistName.text = track.artistName
        trackTime.text = separator + SimpleDateFormat(
            "mm:ss",
            Locale.getDefault()
        ).format(track.trackTimeMillis?.toLong() ?: String())
        artwork.clearColorFilter()

        Glide.with(itemView.context)
            .load(track.artworkUrl100)
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.placeholder)
            .transform(RoundedCorners(16))
            .into(artwork)

        artistName.requestLayout()

        itemView.setOnClickListener{
            adapter.listener.invoke(track)
        }
    }
}