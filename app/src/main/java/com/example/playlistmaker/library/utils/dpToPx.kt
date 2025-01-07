package com.example.playlistmaker.library.utils

import android.content.Context

fun Context.dpToPx(dp: Int): Int = dp * resources.displayMetrics.density.toInt()