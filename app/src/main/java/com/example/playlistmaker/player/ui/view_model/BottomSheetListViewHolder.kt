package com.example.playlistmaker.player.ui.view_model

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ItemBottomsheetPlBinding
import com.example.playlistmaker.library.domain.models.Playlist
import com.example.playlistmaker.library.utils.dpToPx.dpToPx

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
            .transform(CenterCrop(), RoundedCorners(dpToPx(2, itemView.context)))
            .into(binding.plCover)

        binding.plNameTextview.requestLayout()

        binding.root.setOnClickListener {
            listener(playlist)
        }

    }

    private fun formatTrackCount(count: Int): String {
        return itemView.context.resources.getQuantityString(R.plurals.track_count, count, count)
    }
}