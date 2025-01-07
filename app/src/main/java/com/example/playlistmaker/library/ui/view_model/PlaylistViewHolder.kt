package com.example.playlistmaker.library.ui.view_model

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.PlaylistItemBinding
import com.example.playlistmaker.library.domain.models.Playlist
import com.example.playlistmaker.library.utils.dpToPx

class PlaylistViewHolder(
    private val binding: PlaylistItemBinding,
    private val listener: (Playlist) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(playlist: Playlist) {
        binding.playlistTitle.text = playlist.plName
        binding.playlistDescription.text = formatTrackCount(playlist.trackCount)

        Glide.with(itemView.context)
            .load(playlist.plImage)
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.placeholder)
            .transform(CenterCrop(), RoundedCorners(itemView.context.dpToPx(8)))
            .into(binding.playlistImage)

        binding.playlistTitle.requestLayout()

        binding.root.setOnClickListener {
            listener(playlist)
        }

    }

    private fun formatTrackCount(count: Int): String {
        return itemView.context.resources.getQuantityString(R.plurals.track_count, count, count)
    }
}