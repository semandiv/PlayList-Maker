package com.example.playlistmaker.search.ui.activity

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
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.NestedScrollView
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.player.ui.activity.PlayerActivity
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.ui.view_model.SearchViewModel

class SearchActivity : AppCompatActivity() {

    private companion object {
        const val SEARCH_FIELD_KEY = "SearchField"
        const val RECYCLER_STATE_KEY = "Tracks"
        const val SELECTED_TRACK = "selectedTrack"
        const val SEARCH_DEBOUNCE_DELAY = 2000L
        const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    private lateinit var viewModel: SearchViewModel
    private lateinit var binding: ActivitySearchBinding

    private var errorMessage = String()


    private val searchRunnable = Runnable { searchRequest() }
    private val handler = Handler(Looper.getMainLooper())
    private var isClickAlowed = true
    private var isLoading = false



    private val tracks = mutableListOf<Track>()

    private val adapter = TracksAdapter(tracks) { track ->
        saveTrack(track)
        startPlayer(track)
    }

    private var searchQuery = String()

    private lateinit var inputText: EditText
    private lateinit var placeholder: LinearLayout
    private lateinit var historyLayout: NestedScrollView
    private lateinit var trackSearchList: RecyclerView
    private lateinit var placeholderNoResultIcon: ImageView
    private lateinit var placeholderNoConnectIcon: ImageView
    private lateinit var placeholderText: TextView
    private lateinit var refreshBtn: Button
    private lateinit var clearHistoryBtn: Button
    private lateinit var historyAdapter: TracksAdapter
    private lateinit var historyRecyclerView: RecyclerView
    private lateinit var historyList: MutableList<Track>
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //тулбар
        setToolBar()

        viewModel = Creator.provideSearchViewModel(this)

        viewModel.tracks.observe(this, { tracks ->
            handler.post(Runnable {
                showSearchedTracks(tracks)
            })
        })

        viewModel.isLoading.observe(this, { isLoading ->
            progressBar.isVisible = isLoading
        })

        viewModel.loadError.observe(this) {
            handler.post{showResultZeroPlaceholder()}
        }

        viewModel.networkError.observe(this) {
            handler.post{showConnectErrorPlaceholder()}
        }

        historyList = mutableListOf()

        viewModel.history.observe(this, {history->
            historyList.clear()
            historyList.addAll(history)
        })

        viewModel.loadHistory()

        historyAdapter = TracksAdapter(historyList) { track ->
            startPlayer(track)
        }

        inputText = binding.searchEditText
        val clearTextButton = binding.clearText

        placeholder = binding.placeholderSearchView
        historyLayout = binding.historyLayout
        placeholderNoResultIcon = binding.placeholderImageNoResult
        placeholderNoConnectIcon = binding.placeholderImageNoConnect
        placeholderText = binding.placeholderText
        refreshBtn = binding.refreshBtn
        clearHistoryBtn = binding.clearHistoryBtn
        progressBar = binding.progressBar

        placeholder.isVisible = false
        historyLayout.isVisible = false

        inputText.setText(searchQuery)

        clearTextButton.setOnClickListener { view ->
            clearTextField(inputText, view)
        }

        inputText.addTextChangedListener(
            onTextChanged = { text: CharSequence?, _, _, _ ->
                textChageListener(text, clearTextButton)
                searchDebounce()
            }
        )

        inputText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && historyList.size > 0) {
                showHistory()
            } else {
                hideHistory()
            }
        }

        inputText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.searchTracks(inputText.text.toString())
                inputText.hideKeyboard()
                hideHistory()
                true
            } else false
        }

        trackSearchList = findViewById(R.id.trackList)
        trackSearchList.layoutManager = LinearLayoutManager(this)
        trackSearchList.adapter = adapter


        refreshBtn.setOnClickListener {
            refreshBtnClick(inputText)
        }

        historyRecyclerView = binding.historyList
        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true)
        historyRecyclerView.layoutManager = linearLayoutManager
        historyRecyclerView.adapter = historyAdapter

        clearHistoryBtn.setOnClickListener {
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
            trackSearchList.layoutManager?.onSaveInstanceState()
        )
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchQuery = savedInstanceState.getString(SEARCH_FIELD_KEY, String())
        val recyclerState = savedInstanceState.getParcelable<Parcelable>(RECYCLER_STATE_KEY)
        if (recyclerState != null) {
            trackSearchList.layoutManager?.onRestoreInstanceState(recyclerState)
        }
    }

    private fun setToolBar() {
        val toolbar = binding.searchToolbar
        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.search_text)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun clearHistoryBtnClick() {
        hideHistory()
        viewModel.clearHistory()
        historyAdapter.notifyDataSetChanged()
    }

    private fun hideHistory() {
        historyLayout.isVisible = false
    }

    private fun showHistory() {
        placeholder.isVisible = false
        trackSearchList.isVisible = false
        historyAdapter.notifyDataSetChanged()
        historyLayout.isVisible = true
    }

    private fun refreshBtnClick(inputText: EditText) {
        placeholder.isVisible = false
        trackSearchList.isVisible = true
        viewModel.searchTracks(inputText.text.toString())
    }

    private fun textChageListener(text: CharSequence?, clearTextButton: ImageView) {
        if (text != null) {
            searchQuery += text.toString()
            clearTextButton.isVisible = true
        }
    }

    private fun clearTextField(inputText: EditText, view: View) {
        inputText.text = null
        searchQuery = String()
        view.isVisible = false
        tracks.clear()
        adapter.notifyDataSetChanged()
        viewModel.loadHistory()
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

    private fun showTracks() {
        progressBar.isVisible = false
        placeholder.isVisible = false
        historyLayout.isVisible = false
        trackSearchList.isVisible = true
        adapter.notifyDataSetChanged()
        trackSearchList.scrollToPosition(0)
    }

    private fun showResultZeroPlaceholder() {
        progressBar.isVisible = false
        placeholder.isVisible = true
        placeholderNoResultIcon.isVisible = true
        placeholderNoConnectIcon.isVisible = false
        placeholderText.text = getString(R.string.noResultMessage)
        trackSearchList.isVisible = false
        refreshBtn.isVisible = false
    }

    private fun showConnectErrorPlaceholder() {
        progressBar.isVisible = false
        placeholder.isVisible = true
        placeholderNoResultIcon.isVisible = false
        placeholderNoConnectIcon.isVisible = true
        placeholderText.text = this.getString(R.string.noConnectMessage)
        trackSearchList.isVisible = false
        refreshBtn.isVisible = true
    }

    private fun View.hideKeyboard() {
        val inputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
    }

    private fun saveTrack(track: Track) {
        viewModel.saveTrackToHistory(track)
        adapter.notifyItemInserted(0)
    }

    private fun startPlayer(track: Track) {
        if (clickDebounce()) {
            val intent = Intent(this, PlayerActivity::class.java)
            intent.putExtra(SELECTED_TRACK, track)
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
        if (inputText.text.isNotEmpty()) {
            viewModel.searchTracks(inputText.text.toString())
            inputText.hideKeyboard()
            hideHistory()
        }
    }
}