package com.example.playlistmaker.player.ui.activity

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.MenuItem
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.Group
import androidx.core.content.IntentCompat
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityPlayerBinding
import com.example.playlistmaker.player.domain.models.PlayerState
import com.example.playlistmaker.player.ui.view_model.PlayerViewModel
import com.example.playlistmaker.search.domain.models.Track
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity() {

    private companion object {
        const val RECEIVED_TRACK = "selectedTrack"
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

        playerViewModel.playerState.observe(this, { state ->
            when (state) {
                PlayerState.DEFAULT -> binding.playButton.isEnabled = false
                PlayerState.PREPARED -> {
                    binding.playButton.isEnabled = true
                    binding.playButton.setImageResource(R.drawable.baseline_play_circle_filled_84)
                    binding.playingTime.text = DURATION_DEFAULT_VALUE
                }
                PlayerState.PLAYING ->  binding.playButton.setImageResource(R.drawable.baseline_pause_circle_filled_84)
                PlayerState.PAUSED -> binding.playButton.setImageResource(R.drawable.baseline_play_circle_filled_84)
                null -> binding.playButton.isEnabled = false
            }
        })

        playerViewModel.currentPosition.observe(this, { position ->
            binding.playingTime.text = SimpleDateFormat(
                TIME_FORMAT,
                Locale.getDefault()
            ).format(position)
        })

        playerViewModel.isPrepared.observe(this, { prepared ->
            binding.progressBar.isVisible = !prepared
        })

        binding.playButton.setOnClickListener {
            if(playerViewModel.playerState.value == PlayerState.PLAYING) {
                playerViewModel.pause()
            } else {
                playerViewModel.play()
            }
        }

    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
        binding.playButton.setImageResource(R.drawable.baseline_play_circle_filled_84)
    }

    override fun onDestroy() {
        track.previewUrl?.takeIf { !it.isNullOrEmpty() }?.let {
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
        binding.countryValue.text = track.country
        binding.genreValue.text = track.primaryGenreName
        binding.yearValue.text = track.releaseDate?.take(4) ?: String()
        binding.artistName.text = track.artistName
        binding.trackName.text = track.trackName

        binding.timePlayValue.text = SimpleDateFormat(
            TIME_FORMAT,
            Locale.getDefault()
        ).format(track.trackTimeMillis?.toLong() ?: String())

        if (track.collectionName != null) {
            binding.albumNameValue.text = track.collectionName
        } else {
            hideAlbumName()
        }
        getCoverImage()
    }

    private fun getCoverImage() {
        val coverURL = track.artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg")
        Glide.with(this)
            .load(coverURL)
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.placeholder)
            .transform(RoundedCorners(8))
            .into(binding.albumCover)
    }

    private fun hideAlbumName() {
        binding.albumNameGroup.isVisible = false
    }

    private fun pausePlayer() {
        playerViewModel.pause()
    }

    private fun handleNullPreviewUrl() {
        binding.playButton.isEnabled = false
        Toast.makeText(this, getString(R.string.not_load_previewTrack), Toast.LENGTH_SHORT).show()
    }
}