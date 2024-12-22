package com.example.playlistmaker.library.utils

import androidx.recyclerview.widget.DiffUtil
import com.example.playlistmaker.library.domain.models.Playlist

class PlaylistDiffCallback: DiffUtil.ItemCallback<Playlist>() {
    override fun areItemsTheSame(oldItem: Playlist, newItem: Playlist): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: Playlist, newItem: Playlist): Boolean {
        return oldItem == newItem
    }
}