package com.example.playlistmaker.settings.ui.view_model

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class SettingsViewModelFactory(
    private val sharedPref: SharedPreferences
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            return SettingsViewModel(sharedPref) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}