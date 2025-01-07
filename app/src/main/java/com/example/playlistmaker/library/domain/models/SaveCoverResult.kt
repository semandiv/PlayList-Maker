package com.example.playlistmaker.library.domain.models

import android.net.Uri

data class SaveCoverResult(
    val absolutePath: String,
    val uri: Uri
)
