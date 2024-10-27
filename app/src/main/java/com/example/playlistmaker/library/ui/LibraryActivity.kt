package com.example.playlistmaker.library.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityLibraryBinding
import com.google.android.material.tabs.TabLayoutMediator

class LibraryActivity : AppCompatActivity() {

    private var _binding: ActivityLibraryBinding? = null
    private val binding get() = _binding!!

    @SuppressLint("ResourceType", "InflateParams")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityLibraryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setToolbar()

        val adapter = LibraryPagerAdapter(this)
        binding.viewPager.adapter = adapter


        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Избранное"
                1 -> "Плейлисты"
                else -> null
            }
            val customView = layoutInflater.inflate(R.layout.item_tablayout, null)
            val textView = customView.findViewById<TextView>(R.id.tabHeader)
            textView.text = tab.text
            tab.customView = customView
        }.attach()
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
        val toolbar = binding.libraryToolbar
        setSupportActionBar(toolbar)
        supportActionBar?.title = String()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

}