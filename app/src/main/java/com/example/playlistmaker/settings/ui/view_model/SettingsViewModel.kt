package com.example.playlistmaker.settings.ui.view_model

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SettingsViewModel(private val sharedPrefs: SharedPreferences): ViewModel(){

    private companion object{
        const val THEME_SWITCHER = "theme_checker"
        const val SHARE_APP_TEXT = "Поделиться приложением"
        private const val USER_EMAIL = "sand-nlp@ya.ru"
        private const val MAIL_SUBJECT = "Вопрос по приложению"
        private const val MAIL_BODY = "Спасибо разработчикам и разработчицам за крутое приложение!"
        private const val USER_AGREEMENT = "https://yandex.ru/legal/practicum_offer/"
    }

    private val _isDarkTheme = MutableLiveData<Boolean>()
    val isDarkTheme: LiveData<Boolean> get() = _isDarkTheme

    init {
        _isDarkTheme.value = sharedPrefs.getBoolean(THEME_SWITCHER, false)
    }

    fun changeTheme(isChecked: Boolean) {
        _isDarkTheme.value = isChecked
        sharedPrefs.edit().putBoolean(THEME_SWITCHER, isChecked).apply()
    }

    fun getShareIntent(): Intent {
        return Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, SHARE_APP_TEXT)
        }
    }

    fun getSupportEmailIntent(): Intent {
        return Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(USER_EMAIL))
            putExtra(Intent.EXTRA_SUBJECT, MAIL_SUBJECT)
            putExtra(Intent.EXTRA_TEXT, MAIL_BODY)
        }
    }

    fun getUserAgreementIntent(): Intent {
        return Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(USER_AGREEMENT)
        }
    }
}