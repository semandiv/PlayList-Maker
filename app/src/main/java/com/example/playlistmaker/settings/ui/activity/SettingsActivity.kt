package com.example.playlistmaker.settings.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.playlistmaker.App
import com.example.playlistmaker.R
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import com.example.playlistmaker.settings.ui.view_model.SettingsViewModel

const val THEME_SWITCHER = "theme_checker"

class SettingsActivity : AppCompatActivity() {

    private lateinit var settingsViewModel: SettingsViewModel
    private lateinit var binding: ActivitySettingsBinding
    private var currentTheme = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        settingsViewModel = Creator.provideSettingsViewModel(this)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setToolbar()

        settingsViewModel.isDarkTheme.observe(this, { checked ->
            if (currentTheme != checked) {
                binding.themeSwitch.isChecked = checked
                (application as App).switchTheme(checked)
                currentTheme = checked
                //finish()
            }
        })

        binding.themeSwitch.setOnCheckedChangeListener { _, isChecked ->
            settingsViewModel.changeTheme(isChecked)
        }

        binding.shareLayout.setOnClickListener {
            startActivity(
                Intent.createChooser(
                    settingsViewModel.getShareIntent(),
                    getString(R.string.share_header)
                )
            )
        }

        binding.supportLayout.setOnClickListener {
            startActivity(settingsViewModel.getSupportEmailIntent())
        }

        binding.userAgreementLayout.setOnClickListener {
            startActivity(settingsViewModel.getUserAgreementIntent())
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

    private fun setToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.settings_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.settings_title)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}