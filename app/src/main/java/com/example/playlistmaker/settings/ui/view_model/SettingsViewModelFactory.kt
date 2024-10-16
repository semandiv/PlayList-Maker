package com.example.playlistmaker.settings.ui.view_model

import android.app.Application
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.App

class SettingsViewModelFactory(
    private val application: Application,
    private val sharedPref: SharedPreferences
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            return SettingsViewModel(application = application, sharedPref) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}