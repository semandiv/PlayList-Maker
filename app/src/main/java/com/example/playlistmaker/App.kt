package com.example.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.player.di.playerDataModule
import com.example.playlistmaker.player.di.playerViewModelModule
import com.example.playlistmaker.search.di.searchDataModule
import com.example.playlistmaker.search.di.searchDomainModule
import com.example.playlistmaker.search.di.searchViewModelModule
import com.example.playlistmaker.settings.di.settingsViewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

private const val THEME_SWITCH = "theme_checker"

class App : Application() {

    private var darkTheme = false


    override fun onCreate() {
        super.onCreate()
        val sharedPrefs = getSharedPreferences(THEME_SWITCH, MODE_PRIVATE)

        startKoin {
            androidContext(this@App)
            modules(searchDataModule,
                searchDomainModule,
                searchViewModelModule,
                playerDataModule,
                playerViewModelModule,
                settingsViewModelModule)
        }

        //грузим тему из настроек
        switchTheme(sharedPrefs.getBoolean(THEME_SWITCH, false))
    }

    fun switchTheme(isDarkTheme: Boolean) {
        darkTheme = isDarkTheme

        AppCompatDelegate.setDefaultNightMode(
            if (isDarkTheme) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}