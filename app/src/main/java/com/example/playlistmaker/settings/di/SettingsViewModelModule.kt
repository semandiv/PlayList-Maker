package com.example.playlistmaker.settings.di

import android.content.SharedPreferences
import com.example.playlistmaker.settings.ui.view_model.SettingsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

private const val THEME_CHECKER = "theme_checker"

val settingsViewModelModule = module{
    viewModel {
        val sharedPrefs: SharedPreferences = get { parametersOf(THEME_CHECKER) }
        SettingsViewModel(sharedPrefs)}
}