package com.example.playlistmaker.player.ui.activity

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityPlayerBinding
import com.example.playlistmaker.player.domain.models.MediaState
import com.example.playlistmaker.player.ui.view_model.PlayerViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayerActivity : AppCompatActivity() {

    private val playerViewModel: PlayerViewModel by viewModel()

    private lateinit var binding: ActivityPlayerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setToolbar()

        setValues()

        playerViewModel.mediaState.observe(this) { mediaState ->
            when (mediaState) {
                MediaState.Playing -> binding.playButton.setImageResource(R.drawable.baseline_pause_circle_filled_84)
                MediaState.Default -> binding.playButton.isEnabled = false
                MediaState.Paused -> binding.playButton.setImageResource(R.drawable.baseline_play_circle_filled_84)
                is MediaState.Prepared -> {
                    binding.playingTime.text = mediaState.defTime
                    playerPrepared()
                }
            }
        }

        playerViewModel.currentPosition.observe(this) { position ->
            binding.playingTime.text = position
        }

        binding.playButton.setOnClickListener {
            playerViewModel.onClickPlayButton()
        }

    }

    override fun onPause() {
        super.onPause()
        playerViewModel.pause()
        binding.playButton.setImageResource(R.drawable.baseline_play_circle_filled_84)
    }

    override fun onDestroy() {
        playerViewModel.releasePlayer()
        super.onDestroy()
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

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        playerViewModel.getTrack()?.let { setValues() }
    }

    private fun setToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolBar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = String()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setValues() {
        binding.progressBar.isVisible = true
        playerViewModel.getTrack()?.let {
            binding.progressBar.isVisible = true
            binding.countryValue.text = it.country
            binding.genreValue.text = it.primaryGenreName
            binding.yearValue.text = it.releaseDate?.take(4) ?: String()
            binding.artistName.text = it.artistName
            binding.trackName.text = it.trackName
            binding.timePlayValue.text = playerViewModel.timePlay

            if (!it.collectionName.isNullOrEmpty()) {
                binding.albumNameValue.text = it.collectionName
            } else {
                hideAlbumName()
            }
            getCoverImage()
        } ?: run {
            handleNullTrack()
        }
        binding.progressBar.isVisible = false
    }

    private fun getCoverImage() {
        playerViewModel.getCoverUrl()?.let {
            Glide.with(this)
                .load(it)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .transform(RoundedCorners(8))
                .into(binding.albumCover)
        }
    }

    private fun playerPrepared() {
        binding.playButton.isEnabled = true
        binding.playButton.setImageResource(R.drawable.baseline_play_circle_filled_84)
    }

    private fun hideAlbumName() {
        binding.albumNameGroup.isVisible = false
    }

    private fun handleNullTrack() {
        binding.playButton.isEnabled = false
        Toast.makeText(this, getString(R.string.not_load_previewTrack), Toast.LENGTH_SHORT).show()
    }
}