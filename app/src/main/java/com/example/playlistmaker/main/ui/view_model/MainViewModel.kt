package com.example.playlistmaker.main.view_model

import androidx.activity.ComponentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.library.LibraryActivity
import com.example.playlistmaker.search.ui.activity.SearchActivity
import com.example.playlistmaker.settings.ui.activity.SettingsActivity

class MainViewModel: ViewModel(){

    private val _navigateToScreen = MutableLiveData<Class<out ComponentActivity>>()
    val navigateToScreen: LiveData<Class<out ComponentActivity>> = _navigateToScreen

    fun onSearchButtonClicked() {
        _navigateToScreen.value = SearchActivity::class.java
    }

    fun onLibraryButtonClicked() {
        _navigateToScreen.value = LibraryActivity::class.java
    }

    fun onSettingsButtonClicked() {
        _navigateToScreen.value = SettingsActivity::class.java
    }

}