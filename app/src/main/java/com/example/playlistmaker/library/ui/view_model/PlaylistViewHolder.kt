package com.example.playlistmaker.library.ui.view_model

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.PlaylistItemBinding
import com.example.playlistmaker.library.domain.models.Playlist

class PlaylistViewHolder(
    private val binding: PlaylistItemBinding,
    private val listener: (Playlist) -> Unit
): RecyclerView.ViewHolder(binding.root) {

    fun bind(playlist: Playlist) {
        binding.playlistTitle.text = playlist.plName
        binding.playlistDescription.text = formatTrackCount(playlist.trackCount)

        Glide.with(itemView.context)
            .load(playlist.plImage)
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.placeholder)
            .transform(CenterCrop(), RoundedCorners(dpToPx(8, itemView.context)))
            .into(binding.playlistImage)

        binding.playlistTitle.requestLayout()

        binding.root.setOnClickListener {
            listener(playlist)
        }

    }

    private fun dpToPx(dp: Int, context: Context): Int {
        return (dp * context.resources.displayMetrics.density).toInt()
    }

    private fun formatTrackCount(count: Int): String {
        val word = when {
            count % 100 in 11..19 -> "треков"
            count % 10 == 1 -> "трек"
            count % 10 in 2..4 -> "трека"
            else -> "треков"
        }
        return "$count $word"
    }
}