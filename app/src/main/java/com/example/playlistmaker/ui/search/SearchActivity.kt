package com.example.playlistmaker.ui.search

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
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.widget.NestedScrollView
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.Creator
import com.example.playlistmaker.R
import com.example.playlistmaker.data.network.AppleAPI
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.presenter.GetTrackImpl
import com.example.playlistmaker.presenter.HistoryImpl
import com.example.playlistmaker.ui.player.PlayerActivity
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {

    private companion object {
        //const val APPLE_BASE_URL = "https://itunes.apple.com"
        const val SEARCH_FIELD_KEY = "SearchField"
        const val RECYCLER_STATE_KEY = "Tracks"
        //const val SEARCH_HISTORY_KEY = "searchHistory"
        const val SELECTED_TRACK = "selectedTrack"
        const val SEARCH_DEBOUNCE_DELAY = 2000L
        const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    private val searchRunnable = Runnable { searchRequest() }
    private val handler = Handler(Looper.getMainLooper())
    private var newTrackRunnable: Runnable? = null
    private var isClickAlowed = true

/*    private val retrofit = Retrofit.Builder()
        .baseUrl(APPLE_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val appleAPI = retrofit.create(AppleAPI::class.java)*/

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
    private lateinit var searchHistory: HistoryImpl
    private lateinit var historyAdapter: TracksAdapter
    private lateinit var historyRecyclerView: RecyclerView
    private lateinit var historyList: MutableList<Track>
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.search_view)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //инициализация хранилища, нового адаптера и объекта работы с историей поиска

        searchHistory = HistoryImpl(Creator.provideHistoryInteractor())
        historyList = searchHistory.getTracks()
        historyAdapter = TracksAdapter(historyList) { track ->
            startPlayer(track)
        }

        //тулбар
        val toolbar = findViewById<Toolbar>(R.id.search_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.search_text)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        inputText = findViewById<EditText>(R.id.search_edit_text)
        val clearTextButton = findViewById<ImageView>(R.id.clear_text)

        placeholder = findViewById(R.id.placeholderSearchView)
        historyLayout = findViewById(R.id.historyLayout)
        placeholderNoResultIcon = findViewById(R.id.placeholderImageNoResult)
        placeholderNoConnectIcon = findViewById(R.id.placeholderImageNoConnect)
        placeholderText = findViewById(R.id.placeholderText)
        refreshBtn = findViewById(R.id.refreshBtn)
        clearHistoryBtn = findViewById(R.id.clearHistoryBtn)
        progressBar = findViewById(R.id.progressBar)

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
            if (hasFocus && searchHistory.getTracks().size > 0) {
                showHistory()
            } else {
                hideHistory()
            }
        }

        inputText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                searchTracks(inputText)
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

        historyRecyclerView = findViewById(R.id.historyList)
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

    private fun clearHistoryBtnClick() {
        hideHistory()
        searchHistory.clearHistory()
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
        searchTracks(inputText)
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
        val newHistoryList = searchHistory.getTracks()
        if (newHistoryList.isNotEmpty()) {
            historyList.clear()
            historyList.addAll(newHistoryList)
            showHistory()
        } else {
            hideHistory()
        }
        view.hideKeyboard()
    }

    //запрос в сеть
    private fun searchTracks(inputText: EditText) {
        if (inputText.text.isNotEmpty()) {
            progressBar.isVisible = true

            val getTrack = GetTrackImpl()
            getTrack.loadTrack(inputText.text.toString()) {
                handler.post(Runnable {
                    displayTracks(getTrack.getTrack())
                })
            }
        }
    }

    private fun displayTracks(foundTracks: Pair<Int, List<Track>>) {
        val (resultCode, newTracks) = foundTracks
        when (resultCode) {
            200 -> {
                if (newTracks.isNotEmpty()) {
                    tracks.clear()
                    tracks.addAll(newTracks)
                    showTracks()
                } else {
                    showResultZeroPlaceholder()
                }
            }

            400 -> {
                showConnectErrorPlaceholder()
            }

            else -> {
                showResultZeroPlaceholder()
            }

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
        searchHistory.addTrack(track)
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
            searchTracks(inputText)
            inputText.hideKeyboard()
            hideHistory()
        }
    }
}