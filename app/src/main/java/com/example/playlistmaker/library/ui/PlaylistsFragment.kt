package com.example.playlistmaker.library.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistsBinding
import com.example.playlistmaker.library.ui.view_model.PlaylistAdapter
import com.example.playlistmaker.library.ui.view_model.PlaylistViewModel
import com.example.playlistmaker.library.utils.GridSpacingItemDecoration
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
        savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.playlistUpdButton.setOnClickListener {
            findNavController().navigate(R.id.newPlaylistFragment)
        }

        viewModel.playlists.observe(viewLifecycleOwner) { playlists ->
            if (playlists.isNotEmpty()) {
                binding.plPlaceholder.isVisible = false
                adapter.submitList(playlists)
            } else {
                binding.plPlaceholder.isVisible = true
            }
        }

        binding.playlistUpdButton.setOnClickListener {
            findNavController().navigate(R.id.action_playlistsFragment_to_newPlaylistFragment)
        }

        adapter = PlaylistAdapter{}

        val spanCount = 2
        val spacing = dpToPx(8, requireContext())
        val includeEdge = true // Учитывать отступы от краев

        val gridLayoutManager = GridLayoutManager(requireContext(), spanCount)
        binding.playlistList.layoutManager = gridLayoutManager

        val itemDecoration = GridSpacingItemDecoration(spanCount, spacing, includeEdge)
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
    private fun dpToPx(dp: Int, context: Context): Int {
        return (dp * context.resources.displayMetrics.density).toInt()
    }
}
