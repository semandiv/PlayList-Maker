package com.example.playlistmaker.library.ui.view_model

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.playlistmaker.databinding.PlaylistItemBinding
import com.example.playlistmaker.library.domain.models.Playlist
import com.example.playlistmaker.library.utils.PlaylistDiffCallback

class PlaylistAdapter(
    private val listener: (Playlist) -> Unit,
): ListAdapter<Playlist, PlaylistViewHolder>(PlaylistDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val binding = PlaylistItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlaylistViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        val playlist = getItem(position)
        holder.bind(playlist)
    }
}