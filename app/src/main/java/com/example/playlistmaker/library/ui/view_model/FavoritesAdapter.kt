package com.example.playlistmaker.library.ui.view_model

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.playlistmaker.databinding.ItemTrackBinding
import com.example.playlistmaker.library.utils.TrackDiffCallback
import com.example.playlistmaker.search.domain.models.Track

class FavoritesAdapter(
    private val listener: (Track) -> Unit
): ListAdapter<Track, FavoritesViewHolder>(TrackDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesViewHolder {
        val binding = ItemTrackBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoritesViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: FavoritesViewHolder, position: Int) {
        val track = getItem(position)
        holder.bind(track)
    }
}