package com.example.playlistmaker.library.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.databinding.FragmentFavoritesBinding
import com.example.playlistmaker.library.ui.view_model.FavoritesAdapter
import com.example.playlistmaker.library.ui.view_model.FavoritesViewModel
import com.example.playlistmaker.player.ui.activity.PlayerActivity
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

private const val CLICK_DEBOUNCE_DELAY = 1000L

class FavoritesFragment : Fragment() {

    private lateinit var adapter: FavoritesAdapter
    private val favViewModel: FavoritesViewModel by viewModel()
    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    private var clickJob: Job? = null

    private var isClickAllowed = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.favoritesPlaceholder.isVisible = false

        adapter = FavoritesAdapter { track ->
            startPlayer(track)
        }

        binding.favoritesList.layoutManager = LinearLayoutManager(requireContext())
        binding.favoritesList.adapter = adapter


        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                favViewModel.tracks.collect { tracksList ->
                    showPlaceHolder(tracksList.isEmpty())
                    if (tracksList.isNotEmpty()) adapter.submitList(tracksList)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showPlaceHolder(show: Boolean) {
        binding.favoritesList.isVisible = !show
        binding.favoritesPlaceholder.isVisible = show

    }

    private fun startPlayer(track: Track) {
        if (clickDebounce()) {
            favViewModel.playTrack(track)
            val intent = Intent(requireContext(), PlayerActivity::class.java)
            startActivity(intent)
        }
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

    companion object {
        fun newInstance(): FavoritesFragment {
            return FavoritesFragment()
        }
    }
}