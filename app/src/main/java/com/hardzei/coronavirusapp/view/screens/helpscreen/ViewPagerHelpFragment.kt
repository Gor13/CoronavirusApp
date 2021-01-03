package com.hardzei.coronavirusapp.view.screens.helpscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.hardzei.coronavirusapp.R
import kotlinx.android.synthetic.main.fragment_help_view_pager.*

class ViewPagerHelpFragment : Fragment() {

    private val mPageNumbers = COUNT_OF_PAGES

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_help_view_pager, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pagerAdapter =
            HelpScreenSlidePagerAdapter(this, getListOfPagerContents(), mPageNumbers)
        help_pager.adapter = pagerAdapter

        TabLayoutMediator(into_tab_layout, help_pager) { _, _ -> }.attach()
    }

    fun getListOfPagerContents(): List<String> {

        val page1 = "R"
        val page2 = "G"
        val page3 = "B"
        return listOf(page1, page2, page3)
    }
    private companion object {
        const val COUNT_OF_PAGES = 3
    }
}
