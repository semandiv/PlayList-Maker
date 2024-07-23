package com.example.playlistmaker

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.switchmaterial.SwitchMaterial

const val THEME_SWITCHER = "theme_checker"

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_settings)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.settings)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val sharedPrefs = getSharedPreferences(THEME_SWITCHER, MODE_PRIVATE)

        val toolbar = findViewById<Toolbar>(R.id.settings_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.settings_title)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

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
        }
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

    private fun changeTheme(isChecked: Boolean, sharedPrefs: SharedPreferences) {
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

    }
}