package com.example.playlistmaker.library.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentLibraryBinding
import com.google.android.material.tabs.TabLayoutMediator

class LibraryFragment : Fragment() {

    private var _binding: FragmentLibraryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLibraryBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("InflateParams")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar = binding.libraryToolbar
        (activity as? AppCompatActivity)?.setSupportActionBar(toolbar)
        toolbar.title = String()

        val tabLayout = binding.tabLayout
        val viewPager = binding.viewPager

        val navController = findNavController()

        val adapter = LibraryPagerAdapter(requireActivity())
        binding.viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Избранное"
                1 -> "Плейлисты"
                else -> null
            }
            val customView = layoutInflater.inflate(R.layout.item_tablayout, null)
            val textView = customView.findViewById<TextView>(R.id.tabHeader)
            textView.text = tab.text
            tab.customView = customView
        }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}