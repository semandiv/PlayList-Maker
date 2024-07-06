package com.example.playlistmaker

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class SearchActivity : AppCompatActivity() {

    private val tracks = mutableListOf<Track>()

    companion object {
        private const val APPLE_BASE_URL = "https://itunes.apple.com"
        private const val SEARCH_FIELD_KEY  = "SearchField"
    }

    private val retrofit = Retrofit.Builder()
        .baseUrl(Companion.APPLE_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val appleAPI = retrofit.create(AppleAPI::class.java)

    private val adapter = TracksAdapter(tracks)

    private var searchQuery = String()

    private lateinit var placeholder: LinearLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var placeholderNoResultIcon: ImageView
    private lateinit var placeholderNoConnectIcon: ImageView
    private lateinit var placeholderText: TextView
    private lateinit var refreshBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.search_view)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val toolbar = findViewById<Toolbar>(R.id.search_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.search_text)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val inputText = findViewById<EditText>(R.id.search_edit_text)
        val clearTextButton = findViewById<ImageView>(R.id.clear_text)

        placeholder = findViewById(R.id.placeholderSearchView)
        placeholderNoResultIcon = findViewById(R.id.placeholderImageNoResult)
        placeholderNoConnectIcon = findViewById(R.id.placeholderImageNoConnect)
        placeholderText = findViewById(R.id.placeholderText)
        refreshBtn = findViewById(R.id.refreshBtn)

        placeholder.isVisible = false

        inputText.setText(searchQuery)

        clearTextButton.setOnClickListener { view ->
            clearTextField(inputText, view)
        }

        inputText.addTextChangedListener(
            onTextChanged = { text: CharSequence?, _, _, _ ->
                textChageListener(text, clearTextButton)
            }
        )

        inputText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE){
                searchTracks(inputText)
                inputText.hideKeyboard()
                true
            } else false
        }

        recyclerView = findViewById(R.id.trackList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        refreshBtn.setOnClickListener  {
            refreshBtnClick(inputText)
        }
    }

    private fun refreshBtnClick(inputText: EditText) {
        placeholder.isVisible = false
        recyclerView.isVisible = true
        searchTracks(inputText)
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
        tracks.clear()
        adapter.notifyDataSetChanged()
        placeholder.visibility = View.GONE 
        view.hideKeyboard()
    }

    //запрос в сеть
    private fun searchTracks(inputText: EditText) {
        if (inputText.text.isNotEmpty()) {
            appleAPI.searchTrack(inputText.text.toString())
                .enqueue(object : Callback<TracksResponse> {
                    override fun onResponse(
                        call: Call<TracksResponse>,
                        response: Response<TracksResponse>
                    ) {
                        val body = response.body()
                        if (response.code() == 200) {
                            tracks.clear()
                            if (body != null) {
                                if (body.results.isNotEmpty()) {
                                    tracks.addAll(body.results)
                                    showTracks()
                                }
                            }
                            if (tracks.isEmpty()) {
                                showResultZeroPlaceholder()
                            }
                        } else {
                            showResultZeroPlaceholder()
                        }
                    }

                    override fun onFailure(call: Call<TracksResponse>, t: Throwable) {
                        showConnectErrorPlaceholder()
                    }
                })
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showTracks() {
        placeholder.isVisible = false
        recyclerView.isVisible= true
        adapter.notifyDataSetChanged()
    }

    private fun showResultZeroPlaceholder() {
        placeholder.visibility = View.VISIBLE
        placeholderNoResultIcon.visibility = View.VISIBLE
        placeholderNoConnectIcon.visibility = View.GONE
        placeholderText.text = getString(R.string.noResultMessage)
        recyclerView.visibility = View.GONE
        refreshBtn.visibility = View.GONE
    }

    private fun showConnectErrorPlaceholder() {
        placeholder.visibility = View.VISIBLE
        placeholderNoResultIcon.visibility = View.GONE
        placeholderNoConnectIcon.visibility = View.VISIBLE
        placeholderText.text = this.getString(R.string.noConnectMessage)
        recyclerView.visibility = View.GONE
        refreshBtn.visibility = View.VISIBLE
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
        outState.putString("SearchField", searchQuery)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchQuery = savedInstanceState.getString(SEARCH_FIELD_KEY, String())
    }

    private fun View.hideKeyboard() {
        val inputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
    }
}