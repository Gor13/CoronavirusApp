package com.hardzei.coronavirusapp.view.screens.helpscreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.hardzei.coronavirusapp.INTRO_STRING_OBJECT

class HelpScreenSlidePagerAdapter(
    fragment: Fragment,
    val listOfPagerContents: List<String>,
    val mPageNumbers: Int,
) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = mPageNumbers

    override fun createFragment(position: Int): Fragment {
        val fragment = ViewPagerContentHelpFragment()

        when (position) {
            0 ->
                fragment.arguments = Bundle().apply {
                    putStringArray(INTRO_STRING_OBJECT, arrayOf(listOfPagerContents[0]))
                }
            1 ->
                fragment.arguments = Bundle().apply {
                    putStringArray(INTRO_STRING_OBJECT, arrayOf(listOfPagerContents[1]))
                }
            2 ->
                fragment.arguments = Bundle().apply {
                    putStringArray(INTRO_STRING_OBJECT, arrayOf(listOfPagerContents[2]))
                }
        }
        return fragment
    }
}
