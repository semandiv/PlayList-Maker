package com.example.playlistmaker

import android.annotation.SuppressLint
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.text.SimpleDateFormat
import java.util.Locale

class TrackViewHolder(itemView: View, private val adapter: TracksAdapter) : RecyclerView.ViewHolder(itemView), View.OnClickListener{
    private val trackName: TextView = itemView.findViewById(R.id.trackName)
    private val artistName: TextView = itemView.findViewById(R.id.artistName)
    private val artwork: ImageView = itemView.findViewById(R.id.artwork)
    private val trackTime: TextView = itemView.findViewById(R.id.trackTime)

    init {
        itemView.setOnClickListener(this)
    }

    private val separator = " \u2022 "

    @SuppressLint("SetTextI18n")
    fun bind(track: Track) {
        trackName.text = track.trackName
        artistName.text = track.artistName
        trackTime.text = separator + SimpleDateFormat(
            "mm:ss",
            Locale.getDefault()
        ).format(track.trackTimeMillis.toLong())
        artwork.clearColorFilter()

        Glide.with(itemView.context)
            .load(track.artworkUrl100)
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.placeholder)
            .transform(RoundedCorners(16))
            .into(artwork)

        artistName.requestLayout()
    }

    override fun onClick(v: View?) {
        val searchHistory = (itemView.context as SearchActivity).getSearchHistory()
        val position = adapterPosition
        if (position!= RecyclerView.NO_POSITION) {
            val track = adapter.getItem(position)
            searchHistory.addHistory(track)
            adapter.notifyItemInserted(0)
        }
    }
}