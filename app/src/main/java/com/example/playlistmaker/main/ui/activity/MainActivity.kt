package com.example.playlistmaker.main.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.databinding.ActivityMainBinding
import com.example.playlistmaker.main.view_model.MainViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        viewModel.navigateToScreen.observe(this) { activityClass ->
            activityClass?.let {
                val intent = Intent(this@MainActivity, it)
                startActivity(intent)
            }
        }

        binding.searchButton.setOnClickListener {
            viewModel.onSearchButtonClicked()
        }

        binding.libraryButton.setOnClickListener {
            viewModel.onLibraryButtonClicked()
        }

        binding.settingsButton.setOnClickListener {
            viewModel.onSettingsButtonClicked()
        }
    }
}