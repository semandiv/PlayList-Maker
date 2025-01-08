package com.example.playlistmaker.library.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistsBinding
import com.example.playlistmaker.library.ui.view_model.PlaylistAdapter
import com.example.playlistmaker.library.ui.view_model.PlaylistViewModel
import com.example.playlistmaker.library.utils.GridSpacingItemDecoration
import com.example.playlistmaker.library.utils.dpToPx
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment : Fragment() {

    private val viewModel: PlaylistViewModel by viewModel()
    private var _binding: FragmentPlaylistsBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: PlaylistAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch{
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.playlists.collect{playlists->
                    binding.plPlaceholder.isVisible = playlists.isEmpty()
                    if (playlists.isNotEmpty()) adapter.submitList(playlists)
                }
            }
        }

        binding.playlistUpdButton.setOnClickListener {
            findNavController().navigate(R.id.action_playlistsFragment_to_newPlaylistFragment)
        }

        adapter = PlaylistAdapter { playlist ->
            viewModel.openPlaylist(playlist.plID)
            findNavController().navigate(R.id.action_playlistsFragment_to_playlistDetailsFragment)
        }

        val gridLayoutManager = GridLayoutManager(requireContext(), 2)
        binding.playlistList.layoutManager = gridLayoutManager

        val itemDecoration = GridSpacingItemDecoration(2, requireContext().dpToPx(8), true)
        binding.playlistList.addItemDecoration(itemDecoration)

        binding.playlistList.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(): PlaylistsFragment {
            return PlaylistsFragment()
        }
    }
}
