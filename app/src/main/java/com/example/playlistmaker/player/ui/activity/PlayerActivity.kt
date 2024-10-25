package com.example.playlistmaker.player.ui.activity

import android.os.Build
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
import com.example.playlistmaker.search.domain.models.Track
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity() {

    private companion object {
        const val SAVED_TRACK = "savedTrack"
        const val DURATION_DEFAULT_VALUE = "00:00"
        const val TIME_FORMAT = "mm:ss"
    }

    private lateinit var track: Track
    private val playerViewModel: PlayerViewModel by viewModel()

    private lateinit var binding: ActivityPlayerBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.playingTime.text = DURATION_DEFAULT_VALUE


        setToolbar()

        if (playerViewModel.getTrack() != null) {
            track = playerViewModel.getTrack()!!
            setValues()
        } else {
            handleNullPreviewUrl()
        }

        playerViewModel.mediaState.observe(this){ mediaState ->
            when (mediaState) {
                MediaState.Prepared -> playerPrepared()
                MediaState.Playing -> binding.playButton.setImageResource(R.drawable.baseline_pause_circle_filled_84)
                MediaState.Default -> binding.playButton.isEnabled = false
                MediaState.Paused -> binding.playButton.setImageResource(R.drawable.baseline_play_circle_filled_84)
            }
        }

        playerViewModel.currentPosition.observe(this) { position ->
            binding.playingTime.text = SimpleDateFormat(
                TIME_FORMAT,
                Locale.getDefault()
            ).format(position)
        }

        binding.playButton.setOnClickListener {
            playerViewModel.onClickPlayButton()
        }

    }

    private fun playerPrepared() {
        binding.playButton.isEnabled = true
        binding.playButton.setImageResource(R.drawable.baseline_play_circle_filled_84)
        binding.playingTime.text = DURATION_DEFAULT_VALUE
    }

    override fun onPause() {
        super.onPause()
        playerViewModel.pause()
        binding.playButton.setImageResource(R.drawable.baseline_play_circle_filled_84)
    }

    override fun onDestroy() {
        track.previewUrl?.takeIf { it.isNotEmpty() }?.let {
            playerViewModel.releasePlayer()
        }
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable(SAVED_TRACK, track)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        track = savedInstanceState.customGetSerializable<Track>(SAVED_TRACK) as Track
        setValues()
    }

    private fun setToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolBar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = String()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    @Suppress("DEPRECATION")
    private inline fun <reified T : Serializable> Bundle.customGetSerializable(key: String): T? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            getSerializable(key, T::class.java)
        } else {
            getSerializable(key) as? T
        }
    }

    private fun setValues() {
        binding.progressBar.isVisible = true
        binding.countryValue.text = track.country
        binding.genreValue.text = track.primaryGenreName
        binding.yearValue.text = track.releaseDate?.take(4) ?: String()
        binding.artistName.text = track.artistName
        binding.trackName.text = track.trackName

        binding.timePlayValue.text = playerViewModel.timePlay

        if (track.collectionName.isNullOrEmpty()) {
            binding.albumNameValue.text = track.collectionName
        } else {
            hideAlbumName()
        }

        getCoverImage()
        binding.progressBar.isVisible = false
    }

    private fun getCoverImage() {
        if (!playerViewModel.coverUrl.isNullOrEmpty()){
            Glide.with(this)
                .load(playerViewModel.coverUrl)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .transform(RoundedCorners(8))
                .into(binding.albumCover)
        } else{
            handleNullPreviewUrl()
        }
    }

    private fun hideAlbumName() {
        binding.albumNameGroup.isVisible = false
    }

    private fun handleNullPreviewUrl() {
        binding.playButton.isEnabled = false
        Toast.makeText(this, getString(R.string.not_load_previewTrack), Toast.LENGTH_SHORT).show()
    }
}