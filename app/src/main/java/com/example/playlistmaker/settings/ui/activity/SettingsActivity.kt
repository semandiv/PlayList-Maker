package com.example.playlistmaker.settings.ui.activity

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.support.v4.os.IResultReceiver._Parcel
import android.view.MenuItem
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.playlistmaker.App
import com.example.playlistmaker.R
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import com.example.playlistmaker.settings.ui.view_model.SettingsViewModel
import com.google.android.material.switchmaterial.SwitchMaterial

const val THEME_SWITCHER = "theme_checker"

class SettingsActivity : AppCompatActivity() {

    private lateinit var settingsViewModel: SettingsViewModel
    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        settingsViewModel = Creator.provideSettingsViewModel(this)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setToolbar()

        settingsViewModel.isDarkTheme.observe(this, { checked ->
            binding.themeSwitch.isChecked = checked
        })

        binding.themeSwitch.setOnCheckedChangeListener { _, isChecked ->
            settingsViewModel.changeTheme(isChecked)
        }

        binding.shareLayout.setOnClickListener {
            startActivity(Intent.createChooser(settingsViewModel.getShareIntent(), getString(R.string.share_header)))
        }

        binding.supportLayout.setOnClickListener {
            startActivity(settingsViewModel.getSupportEmailIntent())
        }

        binding.userAgreementLayout.setOnClickListener {
            startActivity(settingsViewModel.getUserAgreementIntent())
        }

        /*val sharedPrefs = getSharedPreferences(THEME_SWITCHER, MODE_PRIVATE)



        val themeSwitcher = findViewById<SwitchMaterial>(R.id.theme_switch)
        val shareLayout = findViewById<LinearLayout>(R.id.share_layout)
        val supportLayout = findViewById<LinearLayout>(R.id.support_layout)
        val userAgreementLayout = findViewById<LinearLayout>(R.id.user_agreement_layout)

        themeSwitcher.isChecked = sharedPrefs.getBoolean(THEME_SWITCHER, false)
        (applicationContext as App).switchTheme(themeSwitcher.isChecked)

        themeSwitcher.setOnCheckedChangeListener { _, isChecked ->
            changeTheme(isChecked, sharedPrefs)
        }

        shareLayout.setOnClickListener {
            shareApp()
        }

        supportLayout.setOnClickListener {
            sendSupportEmail()
        }

        userAgreementLayout.setOnClickListener {
            openUserAgreement()
        }*/
    }

    private fun setToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.settings_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.settings_title)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    /*private fun changeTheme(isChecked: Boolean, sharedPrefs: SharedPreferences) {
        (applicationContext as App).switchTheme(isChecked)
        sharedPrefs.edit().putBoolean(THEME_SWITCHER, isChecked).apply()
    }

    private fun shareApp() {
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, getString(R.string.android_dev_url))
        }
        startActivity(Intent.createChooser(shareIntent, getString(R.string.share_header)))
    }

    private fun sendSupportEmail() {
        val mailIntent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.user_email)))
            putExtra(Intent.EXTRA_SUBJECT, getString(R.string.mail_subject))
            putExtra(Intent.EXTRA_TEXT, getString(R.string.mail_body))
        }

        startActivity(mailIntent)

    }

    private fun openUserAgreement() {
        val urlIntent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(getString(R.string.user_agreement))
        }
        startActivity(urlIntent)

    }*/
}