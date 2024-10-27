package com.example.playlistmaker.library.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.playlistmaker.library.fragments.FavoritesFragment
import com.example.playlistmaker.library.fragments.PlaylistsFragment

class LibraryPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FavoritesFragment.newInstance()
            1 -> PlaylistsFragment.newInstance()
            else -> throw IllegalArgumentException("Неправильный индекс")
        }
    }
}
