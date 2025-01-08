package com.example.playlistmaker.player.ui.activity

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityPlayerBinding
import com.example.playlistmaker.library.ui.NewPlaylistFragment
import com.example.playlistmaker.player.domain.models.MediaState
import com.example.playlistmaker.player.domain.models.PlaylistAddResult
import com.example.playlistmaker.player.ui.view_model.BottomSheetListAdapter
import com.example.playlistmaker.player.ui.view_model.PlayerViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayerActivity : AppCompatActivity() {

    private companion object {
        const val BACK_STACK_TAG = "PlayerActivity_BackStack"
    }

    private val playerViewModel: PlayerViewModel by viewModel()

    private lateinit var binding: ActivityPlayerBinding

    private lateinit var adapter: BottomSheetListAdapter

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bottomSheetContainer = binding.plBottomSheet
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetContainer)

        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                binding.overlay.isVisible = newState != BottomSheetBehavior.STATE_HIDDEN
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) { /*NOP*/
            }
        })

        setToolbar()

        setValues()

        adapter = BottomSheetListAdapter { playlist ->
            when (val addResult = playerViewModel.addTrackToPlaylist(playlist)) {
                is PlaylistAddResult.AlreadyExists -> Toast.makeText( //трек уже есть в плейлисте
                    this, getString(R.string.add_track_error_1)
                        .format(addResult.playlistName), Toast.LENGTH_LONG
                ).show()

                PlaylistAddResult.Error -> Toast.makeText( //трек не получилось добавить в плейлист
                    this,
                    getString(R.string.add_track_error_2), Toast.LENGTH_LONG
                ).show()

                is PlaylistAddResult.Success -> Toast.makeText( //трек успешно добавлен
                    this,
                    getString(R.string.new_pl_add_message).format(addResult.playlistName),
                    Toast.LENGTH_LONG
                ).show()

                PlaylistAddResult.TrackIdNotFound -> Toast.makeText( //по какой-то причине не передался ID трека
                    this,
                    getString(R.string.add_track_error_3), Toast.LENGTH_LONG
                ).show()
            }
        }

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                playerViewModel.mediaState.collect { mediaState ->
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
            }
        }

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                playerViewModel.isFavorite.collect { isFavorite ->
                    binding.likeButton.setImageResource(
                        if (isFavorite) R.drawable.like_active else R.drawable.like_unfill
                    )
                }
            }
        }

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                playerViewModel.playlists.collect { playlists ->
                    adapter.submitList(playlists)
                }
            }
        }

        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.playlistRv.layoutManager = linearLayoutManager
        binding.playlistRv.adapter = adapter


        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                playerViewModel.currentPosition.collect { position ->
                    binding.playingTime.text = position
                }
            }
        }

        binding.playButton.setOnClickListener {
            playerViewModel.onClickPlayButton()
        }

        binding.likeButton.setOnClickListener {
            likeButtonClicked()
        }

        binding.addToPlaylist.setOnClickListener {
            adapter.submitList(playerViewModel.playlists.value)
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        binding.newPlBtn.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            openNewPlaylistFragment()
        }

    }

    override fun onPause() {
        super.onPause()
        playerViewModel.pause()
        binding.playButton.setImageResource(R.drawable.baseline_play_circle_filled_84)
    }

    override fun onResume() {
        super.onResume()
        playerViewModel.updatePlayer()
    }

    override fun onDestroy() {
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

    private fun likeButtonClicked() {
        lifecycleScope.launch {
            playerViewModel.toggleFavorite()
        }
    }

    private fun openNewPlaylistFragment() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        val fragment = NewPlaylistFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.main, fragment)
            .addToBackStack(BACK_STACK_TAG)
            .commit()
    }
}