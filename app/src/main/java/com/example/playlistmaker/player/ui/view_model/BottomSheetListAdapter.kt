package com.example.playlistmaker.player.ui.view_model

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.playlistmaker.databinding.ItemBottomsheetPlBinding
import com.example.playlistmaker.library.domain.models.Playlist
import com.example.playlistmaker.library.utils.PlaylistDiffCallback

class BottomSheetListAdapter(
    private val listener: (Playlist) -> Unit
): ListAdapter<Playlist, BottomSheetListViewHolder>(PlaylistDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BottomSheetListViewHolder {
        val binding = ItemBottomsheetPlBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BottomSheetListViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: BottomSheetListViewHolder, position: Int) {
        val playlist = getItem(position)
        holder.bind(playlist)
    }
}