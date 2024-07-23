package com.example.playlistmaker

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
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
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity() {

    private companion object {
        const val RECEIVED_TRACK = "selectedTrack"
        const val SAVED_TRACK = "savedTrack"
    }

    private lateinit var track: Track

    private lateinit var countryValue: TextView
    private lateinit var genreValue: TextView
    private lateinit var yearValue: TextView
    private lateinit var albumNameValue: TextView
    private lateinit var timePlayValue: TextView
    private lateinit var artistName: TextView
    private lateinit var trackName: TextView
    private lateinit var albumCover: ImageView
    private lateinit var albumGroup: Group

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

        setValues()

        val toolbar = findViewById<Toolbar>(R.id.toolBar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = String()
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable(SAVED_TRACK, track)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        track = savedInstanceState.getSerializable(SAVED_TRACK) as Track
        setValues()
    }

    private inline fun <reified T : Serializable> Intent.serializable(key: String): T? = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getSerializableExtra(
            key,
            T::class.java
        )

        else -> @Suppress("DEPRECATION") getSerializableExtra(key) as? T
    }

    private fun setValues() {
        countryValue.text = track.country
        genreValue.text = track.primaryGenreName
        yearValue.text = track.releaseDate.take(4)
        artistName.text = track.artistName
        trackName.text = track.trackName

        timePlayValue.text = SimpleDateFormat(
            "mm:ss",
            Locale.getDefault()
        ).format(track.trackTimeMillis.toLong())

        if (track.collectionName != null) {
            albumNameValue.text = track.collectionName
        } else {
            hideAlbumName()
        }

        getCoverImage()
    }

    private fun getCoverImage() {
        val coverURL = track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")
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
}