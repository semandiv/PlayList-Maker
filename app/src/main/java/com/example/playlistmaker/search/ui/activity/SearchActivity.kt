package com.example.playlistmaker.search.ui.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Parcelable
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.player.ui.activity.PlayerActivity
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.domain.models.TrackSearchResult
import com.example.playlistmaker.search.ui.view_model.SearchViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchActivity : AppCompatActivity() {

    private companion object {
        const val SEARCH_FIELD_KEY = "SearchField"
        const val RECYCLER_STATE_KEY = "Tracks"
        const val SEARCH_DEBOUNCE_DELAY = 2000L
        const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    private val searchViewModel: SearchViewModel by viewModel()
    private lateinit var binding: ActivitySearchBinding

    private val searchRunnable = Runnable { searchRequest() }
    private val handler = Handler(Looper.getMainLooper())
    private var isClickAlowed = true
    private lateinit var historyAdapter: TracksAdapter
    private lateinit var historyList: MutableList<Track>
    private var previousTextLength = 0

    private val tracks = mutableListOf<Track>()

    private val adapter = TracksAdapter(tracks) { track ->
        saveTrack(track)
        startPlayer(track)
    }

    private var searchQuery = String()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setToolBar()

        searchViewModel.searchResult.observe(this) { result ->
            binding.progressBar.isVisible = false
            when (result) {
                TrackSearchResult.NetworkError -> showConnectErrorPlaceholder()
                TrackSearchResult.NoResult -> showResultZeroPlaceholder()
                is TrackSearchResult.SearchResult -> showSearchedTracks(result.tracks)
            }
        }

        historyList = mutableListOf()

        searchViewModel.history.observe(this) { history ->
            historyList.clear()
            historyList.addAll(history)
        }

        searchViewModel.loadHistory()

        historyAdapter = TracksAdapter(historyList) { track ->
            startPlayer(track)
        }

        binding.placeholderSearchView.isVisible = false
        binding.historyLayout.isVisible = false

        binding.searchEditText.setText(searchQuery)

        binding.clearText.setOnClickListener { view ->
            clearTextField(binding.searchEditText, view)
        }

        binding.searchEditText.addTextChangedListener(
            beforeTextChanged = {text: CharSequence?, _, _, _ ->
                previousTextLength = text?.length ?: 0
            },
            onTextChanged = { text: CharSequence?, _, _, _ ->
                textChageListener(text, binding.clearText)
                searchDebounce()
            },
            afterTextChanged = { text: CharSequence? ->
                val backSpaceOnPressed = previousTextLength > (text?.length ?: 0)

                if (backSpaceOnPressed) {
                    binding.trackList.isVisible = false
                }
            }
        )

        binding.searchEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && historyList.size > 0) {
                showHistory()
            } else {
                hideHistory()
            }
        }

        binding.searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                searchViewModel.searchedTracks(binding.searchEditText.text.toString())
                binding.searchEditText.hideKeyboard()
                hideHistory()
                true
            } else false
        }

        binding.trackList.layoutManager = LinearLayoutManager(this)
        binding.trackList.adapter = adapter


        binding.refreshBtn.setOnClickListener {
            refreshBtnClick(binding.searchEditText)
        }

        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true)
        binding.historyList.layoutManager = linearLayoutManager
        binding.historyList.adapter = historyAdapter

        binding.clearHistoryBtn.setOnClickListener {
            clearHistoryBtnClick()
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_FIELD_KEY, searchQuery)
        outState.putParcelable(
            RECYCLER_STATE_KEY,
            binding.trackList.layoutManager?.onSaveInstanceState()
        )
    }

    @Suppress("DEPRECATION")
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchQuery = savedInstanceState.getString(SEARCH_FIELD_KEY, String())
        val recyclerState = savedInstanceState.getParcelable<Parcelable>(RECYCLER_STATE_KEY)
        if (recyclerState != null) {
            binding.trackList.layoutManager?.onRestoreInstanceState(recyclerState)
        }
    }

    private fun setToolBar() {
        val toolbar = binding.searchToolbar
        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.search_text)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun clearHistoryBtnClick() {
        hideHistory()
        searchViewModel.clearHistory()
        historyAdapter.notifyDataSetChanged()
    }

    private fun hideHistory() {
        binding.historyLayout.isVisible = false
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showHistory() {
        binding.placeholderSearchView.isVisible = false
        binding.trackList.isVisible = false
        historyAdapter.notifyDataSetChanged()
        binding.historyLayout.isVisible = true
    }

    private fun refreshBtnClick(inputText: EditText) {
        binding.placeholderSearchView.isVisible = false
        binding.trackList.isVisible = true
        searchViewModel.searchedTracks(inputText.text.toString())
    }

    private fun textChageListener(text: CharSequence?, clearTextButton: ImageView) {
        if (text != null) {
            searchQuery += text.toString()
            clearTextButton.isVisible = true
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun clearTextField(inputText: EditText, view: View) {
        inputText.text = null
        searchQuery = String()
        view.isVisible = false
        tracks.clear()
        adapter.notifyDataSetChanged()
        searchViewModel.loadHistory()
        if (historyList.isNotEmpty()) {
            showHistory()
        } else {
            hideHistory()
        }
        view.hideKeyboard()
    }

    private fun showSearchedTracks(newTracks: List<Track>) {
        if (newTracks.isNotEmpty()) {
            tracks.clear()
            tracks.addAll(newTracks)
            showTracks()
        } else {
            showResultZeroPlaceholder()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showTracks() {
        binding.progressBar.isVisible = false
        binding.placeholderSearchView.isVisible = false
        binding.historyLayout.isVisible = false
        binding.trackList.isVisible = true
        adapter.notifyDataSetChanged()
        binding.trackList.scrollToPosition(0)
    }

    private fun showResultZeroPlaceholder() {
        binding.progressBar.isVisible = false
        binding.placeholderSearchView.isVisible = true
        binding.placeholderImageNoResult.isVisible = true
        binding.placeholderImageNoConnect.isVisible = false
        binding.placeholderText.text = getString(R.string.noResultMessage)
        binding.trackList.isVisible = false
        binding.refreshBtn.isVisible = false
    }

    private fun showConnectErrorPlaceholder() {
        binding.progressBar.isVisible = false
        binding.placeholderSearchView.isVisible = true
        binding.placeholderImageNoResult.isVisible = false
        binding.placeholderImageNoConnect.isVisible = true
        binding.placeholderText.text = this.getString(R.string.noConnectMessage)
        binding.trackList.isVisible = false
        binding.refreshBtn.isVisible = true
    }

    private fun View.hideKeyboard() {
        val inputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
    }

    private fun saveTrack(track: Track) {
        searchViewModel.saveTrackToHistory(track)
        adapter.notifyItemInserted(0)
    }

    private fun startPlayer(track: Track) {
        if (clickDebounce()) {
            searchViewModel.playTrack(track)
            val intent = Intent(this, PlayerActivity::class.java)
            startActivity(intent)
        }
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAlowed
        if (isClickAlowed) {
            isClickAlowed = false
            handler.postDelayed({ isClickAlowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    private fun searchRequest() {
        if (binding.searchEditText.text.isNotEmpty()) {
            binding.progressBar.isVisible = true
            searchViewModel.searchedTracks(binding.searchEditText.text.toString())
            binding.searchEditText.hideKeyboard()
            hideHistory()
        }
    }
}