package com.example.playlistmaker.library.ui.view_model

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.PlaylistItemBinding
import com.example.playlistmaker.library.domain.models.Playlist

class PlaylistViewHolder(
    private val binding: PlaylistItemBinding,
    private val listener: (Playlist) -> Unit
): RecyclerView.ViewHolder(binding.root) {

    fun bind(playlist: Playlist) {
        binding.playlistTitle.text = playlist.name
        binding.playlistDescription.text = playlist.description

        Glide.with(itemView.context)
            .load(playlist.cover)
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.placeholder)
            .transform(RoundedCorners(dpToPx(8, itemView.context)))
            .into(binding.playlistImage)

        binding.playlistTitle.requestLayout()

        binding.root.setOnClickListener {
            listener(playlist)
        }

    }

    private fun dpToPx(dp: Int, context: Context): Int {
        return (dp * context.resources.displayMetrics.density).toInt()
    }
}