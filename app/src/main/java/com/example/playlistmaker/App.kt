package com.example.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

private const val THEME_SWITCH = "theme_checker"
private const val SEARCH_HISTORY_KEY = "searchHistory"

class App : Application() {

    private var darkTheme = false

    override fun onCreate() {
        super.onCreate()
        val sharedPrefs = getSharedPreferences(THEME_SWITCH, MODE_PRIVATE)
        //грузим тему из настроек
        switchTheme(sharedPrefs.getBoolean(THEME_SWITCH, false))
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        if (darkTheme != darkThemeEnabled) {
            darkTheme = darkThemeEnabled
            AppCompatDelegate.setDefaultNightMode(
                if (darkThemeEnabled) {
                    AppCompatDelegate.MODE_NIGHT_YES
                } else {
                    AppCompatDelegate.MODE_NIGHT_NO
                }
            )
        }
    }
}