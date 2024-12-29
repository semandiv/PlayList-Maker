package com.example.playlistmaker.player.ui.view_model

import android.annotation.SuppressLint
import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ItemBottomsheetPlBinding
import com.example.playlistmaker.library.domain.models.Playlist

class BottomSheetListViewHolder(
    private val binding: ItemBottomsheetPlBinding,
    private val listener: (Playlist) -> Unit
): RecyclerView.ViewHolder(binding.root) {

    @SuppressLint("SetTextI18n")
    fun bind(playlist: Playlist) {
        binding.plNameTextview.text = playlist.plName
        binding.plTrackcount.text = formatTrackCount(playlist.trackCount)

        Glide.with(itemView.context)
            .load(playlist.plImage)
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.placeholder)
            .transform(RoundedCorners(dpToPx(8, itemView.context)))
            .into(binding.plCover)

        binding.plNameTextview.requestLayout()

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