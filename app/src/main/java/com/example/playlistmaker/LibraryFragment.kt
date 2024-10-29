package com.example.playlistmaker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentLibraryBinding
import com.example.playlistmaker.library.ui.LibraryPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class LibraryFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null

    private var _binding: FragmentLibraryBinding? = null
    val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLibraryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar = binding.libraryToolbar
        (activity as? AppCompatActivity)?.setSupportActionBar(toolbar)
        toolbar.title = getString(R.string.libraryTitle)

        val tabLayout = binding.tabLayout
        val viewPager = binding.viewPager

        // Устанавливаем адаптер для ViewPager
        val adapter = LibraryPagerAdapter(requireActivity())
        binding.viewPager.adapter = adapter

        // Настраиваем TabLayoutMediator
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


    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LibraryFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}