package com.example.playlistmaker.ui.player

import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.Group
import androidx.core.content.IntentCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.models.Track
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity() {

    private companion object {
        const val RECEIVED_TRACK = "selectedTrack"
        const val SAVED_TRACK = "savedTrack"
        const val DELAY = 300L
        const val DURATION_DEFAULT_VALUE = "00:00"
    }

    enum class PlayerState {
        DEFAULT,
        PREPARED,
        PLAYING,
        PAUSED
    }

    private lateinit var track: Track

    private var playerState = PlayerState.DEFAULT
    private val mediaPlayer = MediaPlayer()
    private var currentPosition = 0
    private var handler: Handler? = null

    private lateinit var countryValue: TextView
    private lateinit var genreValue: TextView
    private lateinit var yearValue: TextView
    private lateinit var albumNameValue: TextView
    private lateinit var timePlayValue: TextView
    private lateinit var artistName: TextView
    private lateinit var trackName: TextView
    private lateinit var albumCover: ImageView
    private lateinit var albumGroup: Group
    private lateinit var playButton: ImageView
    private lateinit var playingTime: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_player)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        IntentCompat.getSerializableExtra(intent, RECEIVED_TRACK, Track::class.java)?.let {
            track = it
        }

        countryValue = findViewById(R.id.countryValue)
        genreValue = findViewById(R.id.genreValue)
        yearValue = findViewById(R.id.yearValue)
        albumNameValue = findViewById(R.id.albumNameValue)
        timePlayValue = findViewById(R.id.timePlayValue)
        artistName = findViewById(R.id.artistName)
        trackName = findViewById(R.id.trackName)
        albumCover = findViewById(R.id.albumCover)
        albumGroup = findViewById(R.id.albumNameGroup)

        playButton = findViewById(R.id.playButton)
        playingTime = findViewById(R.id.playingTime)
        playingTime.text = DURATION_DEFAULT_VALUE

        setValues()

        val toolbar = findViewById<Toolbar>(R.id.toolBar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = String()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        handler = Handler(Looper.getMainLooper())
        preparePlayer()

        playButton.setOnClickListener {
            playerControl()
        }

    }

    override fun onPause() {
        super.onPause()
        stopTimeUpdate()
        pausePlayer()
        playButton.setImageResource(R.drawable.baseline_play_circle_filled_84)
    }

    override fun onDestroy() {
        super.onDestroy()
        stopTimeUpdate()
        mediaPlayer.release()
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

    @Suppress("DEPRECATION")
    private inline fun <reified T : Serializable> Bundle.customGetSerializable(key: String): T? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            getSerializable(key, T::class.java)
        } else {
            getSerializable(key) as? T
        }
    }

    private fun setValues() {
        countryValue.text = track.country
        genreValue.text = track.primaryGenreName
        yearValue.text = track.releaseDate?.take(4) ?: String()
        artistName.text = track.artistName
        trackName.text = track.trackName

        timePlayValue.text = SimpleDateFormat(
            "mm:ss",
            Locale.getDefault()
        ).format(track.trackTimeMillis?.toLong() ?: String())

        if (track.collectionName != null) {
            albumNameValue.text = track.collectionName
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
            .into(albumCover)
    }

    private fun hideAlbumName() {
        albumGroup.isVisible = false
    }

    private fun preparePlayer() {
        if (track.previewUrl?.isNotEmpty() == true) {
            mediaPlayer.setDataSource(track.previewUrl)
            mediaPlayer.prepareAsync()

            mediaPlayer.setOnPreparedListener {
                playerState = PlayerState.PREPARED
            }
            mediaPlayer.setOnCompletionListener {
                playerState = PlayerState.PREPARED
                playingTime.text = DURATION_DEFAULT_VALUE
                playButton.setImageResource(R.drawable.baseline_play_circle_filled_84)
            }
        }
    }

    private fun startPlayer() {
        playerState = PlayerState.PLAYING
        mediaPlayer.start()
    }

    private fun pausePlayer() {
        playerState = PlayerState.PAUSED
        mediaPlayer.pause()
    }

    private fun playerControl() {
        when (playerState) {
            PlayerState.PLAYING -> {
                stopTimeUpdate()
                pausePlayer()
                playButton.setImageResource(R.drawable.baseline_play_circle_filled_84)
            }

            PlayerState.PREPARED, PlayerState.PAUSED -> {
                startTimeUpdate()
                startPlayer()
                playButton.setImageResource(R.drawable.baseline_pause_circle_filled_84)

            }

            else -> {
                Toast.makeText(this, "Плеер не готов к работе", Toast.LENGTH_SHORT).show()}
        }
    }

    private fun timeUpdate(): Runnable {
        return object : Runnable {
            override fun run() {
                if (playerState == PlayerState.PLAYING) {
                    currentPosition = mediaPlayer.currentPosition
                    Log.d("CURRENT_TIME_POSITION", mediaPlayer.currentPosition.toString())
                    playingTime.text = SimpleDateFormat(
                        "mm:ss",
                        Locale.getDefault()
                    ).format(currentPosition)
                    handler?.postDelayed(this, DELAY)
                } else {
                    handler?.removeCallbacks(this)
                }
            }
        }
    }

    private fun startTimeUpdate() {
        handler?.post(timeUpdate())
    }

    private fun stopTimeUpdate() {
        handler?.removeCallbacks(timeUpdate())
    }
}