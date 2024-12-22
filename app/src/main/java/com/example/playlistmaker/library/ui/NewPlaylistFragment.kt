package com.example.playlistmaker.library.ui

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.RootActivity
import com.example.playlistmaker.databinding.FragmentNewPlaylistBinding
import com.example.playlistmaker.library.ui.view_model.NewPlaylistViewModel

class NewPlaylistFragment : Fragment() {

    companion object {
        fun newInstance() = NewPlaylistFragment()
    }

    private var _binding: FragmentNewPlaylistBinding? = null
    private val binding get() = _binding!!

    private val viewModel: NewPlaylistViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolBar = binding.toolBar
        (activity as? AppCompatActivity)?.apply {
            setSupportActionBar(toolBar)
            supportActionBar?.apply {
                title = "Новый плейлист"
                setDisplayHomeAsUpEnabled(true) // Включаем кнопку "назад"
            }
        }

        toolBar.setNavigationOnClickListener {
            findNavController().popBackStack() // Возврат к предыдущему экрану
        }

        (activity as? RootActivity)?.hideBottomNavigationView()


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        (activity as? RootActivity)?.showBottomNavigationView()
    }


}