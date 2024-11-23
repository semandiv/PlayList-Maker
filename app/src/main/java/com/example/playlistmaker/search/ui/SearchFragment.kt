package com.example.playlistmaker.search.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentSearchBinding
import com.example.playlistmaker.player.ui.activity.PlayerActivity
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.domain.models.TrackSearchResult
import com.example.playlistmaker.search.ui.view_model.SearchViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class SearchFragment : Fragment() {

    private val searchViewModel: SearchViewModel by viewModel()
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private var searchJob: Job?= null
    private var clickJob: Job?= null

    private var isClickAllowed = true
    private lateinit var historyAdapter: TracksAdapter
    private lateinit var historyList: MutableList<Track>
    private var previousTextLength = 0

    private val tracks = mutableListOf<Track>()

    private val adapter = TracksAdapter(tracks) { track ->
        saveTrack(track)
        startPlayer(track)
    }

    private var searchQuery = String()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar = binding.searchToolbar
        (activity as? AppCompatActivity)?.setSupportActionBar(toolbar)
        toolbar.title = getString(R.string.search_text)


        searchViewModel.searchResult.observe(viewLifecycleOwner) { result ->
            binding.progressBar.isVisible = false
            when (result) {
                TrackSearchResult.NetworkError -> showPlaceHolders(false)
                TrackSearchResult.NoResult -> showPlaceHolders(true)
                is TrackSearchResult.SearchResult -> showSearchedTracks(result.tracks)
            }
        }

        historyList = mutableListOf()

        searchViewModel.history.observe(viewLifecycleOwner) { history ->
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

        binding.clearText.isVisible = false
        binding.clearText.setOnClickListener {
            clearTextField(binding.searchEditText, it)
        }

        binding.searchEditText.addTextChangedListener(
            beforeTextChanged = { text: CharSequence?, _, _, _ ->
                previousTextLength = text?.length ?: 0
            },
            onTextChanged = { text: CharSequence?, _, _, _ ->
                binding.clearText.isVisible = !text.isNullOrEmpty()
                textChangeListener(text, binding.clearText)
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
                hideHistory()
                true
            } else false
        }

        binding.trackList.layoutManager = LinearLayoutManager(requireContext())
        binding.trackList.adapter = adapter


        binding.refreshBtn.setOnClickListener {
            refreshBtnClick(binding.searchEditText)
        }

        val linearLayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, true)
        binding.historyList.layoutManager = linearLayoutManager
        binding.historyList.adapter = historyAdapter

        binding.clearHistoryBtn.setOnClickListener {
            clearHistoryBtnClick()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val SEARCH_DEBOUNCE_DELAY = 2000L
        const val CLICK_DEBOUNCE_DELAY = 1000L
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

    private fun textChangeListener(text: CharSequence?, clearTextButton: ImageView) {
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
        hideKeyboard()
    }

    private fun showSearchedTracks(newTracks: List<Track>) {
        if (newTracks.isNotEmpty()) {
            tracks.clear()
            tracks.addAll(newTracks)
            showTracks()
        } else {
            showPlaceHolders(true)
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

    private fun showPlaceHolders(zeroValue: Boolean) {
        //true если нет результата и false если ошибка сети/нет соединения
        binding.progressBar.isVisible = false
        binding.placeholderSearchView.isVisible = true
        binding.placeholderImageNoResult.isVisible = zeroValue
        binding.placeholderImageNoConnect.isVisible = !zeroValue
        if (zeroValue) {
            binding.placeholderText.text = getString(R.string.noResultMessage)
        } else binding.placeholderText.text = this.getString(R.string.noConnectMessage)
        binding.trackList.isVisible = false
        binding.refreshBtn.isVisible = !zeroValue
    }

    private fun saveTrack(track: Track) {
        searchViewModel.saveTrackToHistory(track)
        adapter.notifyItemInserted(0)
    }

    private fun startPlayer(track: Track) {
        if (clickDebounce()) {
            searchViewModel.playTrack(track)
            val intent = Intent(requireContext(), PlayerActivity::class.java)
            startActivity(intent)
        }
    }

    private fun hideKeyboard() {
        val inputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val view = requireActivity().currentFocus ?: View(requireContext())
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            clickJob?.cancel()
            clickJob = lifecycleScope.launch {
                delay(CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }
        }
        return current
    }

    private fun searchDebounce() {
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            delay(SEARCH_DEBOUNCE_DELAY)
            searchRequest()
        }
    }

    private fun searchRequest() {
        val query = binding.searchEditText.text
        if (query.isNotEmpty() && query.toString() != searchQuery) {
            binding.progressBar.isVisible = true
            searchViewModel.searchedTracks(query.toString())
            hideKeyboard()
            hideHistory()
        }
    }
}