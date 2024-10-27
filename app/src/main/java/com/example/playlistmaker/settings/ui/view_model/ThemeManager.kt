package com.example.playlistmaker.settings.ui.view_model

import android.content.Context
import com.example.playlistmaker.App

class ThemeManager(private val context: Context): ThemeSwitcher {

    override fun switchTheme(isDarkTheme: Boolean) {
        val app = context.applicationContext as App
        app.switchTheme(isDarkTheme)
    }

}