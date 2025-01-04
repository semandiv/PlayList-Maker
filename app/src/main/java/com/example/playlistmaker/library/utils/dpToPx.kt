package com.example.playlistmaker.library.utils

import android.content.Context

object dpToPx {
    fun dpToPx(dp: Int, context: Context): Int {
        return (dp * context.resources.displayMetrics.density).toInt()
    }
}