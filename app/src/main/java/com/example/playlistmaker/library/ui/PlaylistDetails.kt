package com.example.playlistmaker.library.ui

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.playlistmaker.RootActivity
import com.example.playlistmaker.databinding.FragmentPlaylistDetailsBinding
import com.example.playlistmaker.library.domain.models.Playlist
import com.example.playlistmaker.library.ui.view_model.PlaylistViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistDetails : Fragment() {

    private val viewModel: PlaylistViewModel by viewModel()
    private var _binding: FragmentPlaylistDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistDetailsBinding.inflate(inflater, container, false)
        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val toolBar = binding.toolBar
        (activity as? AppCompatActivity)?.apply {
            setSupportActionBar(toolBar)
            supportActionBar?.apply {
                title = String()
                setDisplayHomeAsUpEnabled(true)
            }
        }

        toolBar.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }

        (activity as? RootActivity)?.hideBottomNavigationView()

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            parentFragmentManager.popBackStack()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.selectedPlaylist.collect { playlist ->
                    playlist?.let {
                        loadPlaylist(it)
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as? RootActivity)?.showBottomNavigationView()
        _binding = null
    }

    private fun loadPlaylist(playlist: Playlist) {
        binding.playlistCover.setImageURI(Uri.parse(playlist.plImage))
        binding.playlistName.text = playlist.plName
        binding.playlistDescription.text = playlist.plDescription
    }
}