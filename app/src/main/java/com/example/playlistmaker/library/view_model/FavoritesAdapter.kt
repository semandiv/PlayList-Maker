package com.example.playlistmaker.library.view_model

import android.view.LayoutInflater
import com.example.playlistmaker.search.domain.models.Track
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.ItemTrackBinding

class FavoritesAdapter(
    private val tracks: List<Track>,
    val listener: (Track) -> Unit
): RecyclerView.Adapter<FavoritesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesViewHolder {
        val binding = ItemTrackBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoritesViewHolder(binding, this)
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

    override fun onBindViewHolder(holder: FavoritesViewHolder, position: Int) {
        holder.bind(tracks[position])
    }
}