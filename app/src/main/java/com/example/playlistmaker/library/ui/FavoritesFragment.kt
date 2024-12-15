package com.example.playlistmaker.library.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
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

class FavoritesFragment : Fragment() {

    private val favViewModel: FavoritesViewModel by viewModel()
    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    private val tracks = mutableListOf<Track>()

    private var clickJob: Job?= null

    private var isClickAllowed = true

    private val adapter = FavoritesAdapter(tracks) { track ->
        startPlayer(track)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.favoritesPlaceholder.isVisible = false


        favViewModel.tracks.observe(viewLifecycleOwner, Observer { tracksList ->
            if (tracksList.isEmpty()) {
                showPlaceHolder()
            } else {
                tracks.clear()
                tracks.addAll(tracksList)
                adapter.notifyDataSetChanged()
            }
        })

        binding.favoritesList.layoutManager = LinearLayoutManager(requireContext())
        binding.favoritesList.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        favViewModel.getTracks()
    }

    private fun showPlaceHolder() {
        binding.favoritesList.isVisible = false
        binding.favoritesPlaceholder.isVisible = true

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
        const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}