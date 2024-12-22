package com.example.playlistmaker.library.ui.view_model

import android.annotation.SuppressLint
import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ItemTrackBinding
import com.example.playlistmaker.search.domain.models.Track
import java.text.SimpleDateFormat
import java.util.Locale

class FavoritesViewHolder(
    private val binding: ItemTrackBinding,
    private val listener: (Track) -> Unit
): RecyclerView.ViewHolder(binding.root) {

    private companion object{
        const val SEPARATOR = " \u2022 "
    }


    @SuppressLint("SetTextI18n")
    fun bind(track: Track) {
        binding.trackName.text = track.trackName
        binding.artistName.text = track.artistName
        binding.trackTime.text = SEPARATOR + SimpleDateFormat(
            "mm:ss",
            Locale.getDefault()
        ).format(track.trackTimeMillis?.toLong() ?: String())
        binding.artwork.clearColorFilter()

        Glide.with(itemView.context)
            .load(track.artworkUrl100)
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.placeholder)
            .transform(RoundedCorners(dpToPx(16, itemView.context)))
            .into(binding.artwork)

        binding.artistName.requestLayout()

        binding.root.setOnClickListener { listener(track) }
    }

    private fun dpToPx(dp: Int, context: Context): Int {
        return (dp * context.resources.displayMetrics.density).toInt()
    }
}