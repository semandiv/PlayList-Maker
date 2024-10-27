package com.example.playlistmaker.settings.ui.view_model

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SettingsViewModel(
    private val sharedPrefs: SharedPreferences,
    private val themeSwitcher: ThemeSwitcher
): ViewModel(){

    private companion object{
        const val THEME_SWITCHER = "theme_checker"
    }

    private val _isDarkTheme = MutableLiveData<Boolean>()
    val isDarkTheme: LiveData<Boolean> get() = _isDarkTheme

    init {
        _isDarkTheme.value = sharedPrefs.getBoolean(THEME_SWITCHER, false)
    }

    fun changeTheme(isChecked: Boolean) {
        _isDarkTheme.value = isChecked
        sharedPrefs.edit().putBoolean(THEME_SWITCHER, isChecked).apply()
        themeSwitcher.switchTheme(isChecked)
    }

}