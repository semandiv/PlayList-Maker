package com.example.playlistmaker

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class TracksAdapter(private val tracks: ArrayList<Track>, val listener: ClickListener) : RecyclerView.Adapter<TrackViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_track, parent, false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position], listener)
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

    interface ClickListener {
        fun onTrackClick(track: Track)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateAdapter(tracks: ArrayList<Track>) {
        this.tracks.clear()
        this.tracks.addAll(tracks)
        notifyDataSetChanged()
    }
}