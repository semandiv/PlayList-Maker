package com.example.playlistmaker.library.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.databinding.FragmentFavoritesBinding
import com.example.playlistmaker.library.view_model.FavoritesAdapter
import com.example.playlistmaker.library.view_model.FavoritesViewModel
import com.example.playlistmaker.search.domain.models.Track
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritesFragment : Fragment() {

    private val favViewModel: FavoritesViewModel by viewModel()
    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    private val tracks = mutableListOf<Track>()

    private val adapter = FavoritesAdapter(tracks) { track ->
        favViewModel.playTrack(track)
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


        favViewModel.tracks.observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                showPlaceHolder()
            } else {
                tracks.clear()
                tracks.addAll(it)
                adapter.notifyDataSetChanged()
            }
        }

        binding.favoritesList.layoutManager = LinearLayoutManager(requireContext())
        binding.favoritesList.adapter = adapter
    }

    private fun showPlaceHolder() {
        binding.favoritesList.isVisible = false
        binding.favoritesPlaceholder.isVisible = true

    }

    companion object {
        fun newInstance(): FavoritesFragment {
            return FavoritesFragment()
        }
    }
}