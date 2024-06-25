package com.example.playlistmaker

import android.app.Activity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SearchActivity : AppCompatActivity() {

    private var searchQuery = String()
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

        inputText.setText(searchQuery)

        clearTextButton.setOnClickListener {view ->
            inputText.text = null
            searchQuery = String()
            view.hideKeyboard()
        }

        inputText.addTextChangedListener(
            onTextChanged = { text: CharSequence?, _, _,_ ->
                searchQuery += text.toString()
                clearTextButton.isVisible = !text.isNullOrEmpty()
            }

        )

        val tracks = Track.createMockTracks()

        val recyclerView = findViewById<RecyclerView>(R.id.trackList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = TracksAdapter(tracks)
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
        outState.putString("SearchField", searchQuery )
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchQuery = savedInstanceState.getString("SearchField","")

    }

    private fun View.hideKeyboard() {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
    }
}