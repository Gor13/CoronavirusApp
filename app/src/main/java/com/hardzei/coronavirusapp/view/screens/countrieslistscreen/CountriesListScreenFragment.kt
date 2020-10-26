package com.hardzei.coronavirusapp.view.screens.countrieslistscreen

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.hardzei.coronavirusapp.R
import com.hardzei.coronavirusapp.view.adapters.CountryListAdapter
import com.hardzei.coronavirusapp.view.formatToStringWithDiv
import com.hardzei.coronavirusapp.view.screens.detailcountryscreen.DetailCountryScreenFragment
import com.hardzei.coronavirusapp.view.screens.settingscreen.SettingsScreenFragment
import com.hardzei.coronavirusapp.viewmodel.CountriesListViewModel
import kotlinx.android.synthetic.main.fragment_main_screen.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class CountriesListScreenFragment : Fragment() {

    private val countriesListViewModel by viewModel<CountriesListViewModel>()
    private lateinit var adapter: CountryListAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var spinner: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.detail_menu, menu)
        menu.close()
        return super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.settings_item) {
            parentFragmentManager
                .beginTransaction()
                .setCustomAnimations(R.animator.back_animator, R.animator.front_animator)
                .replace(
                    R.id.frame_container,
                    SettingsScreenFragment()
                ).addToBackStack(MAIN_SCREEN_FRAGMENT_TAG)
                .commit()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = recyclerViewWithCountries
        spinner = spinner_with_sort_items

        adapter = CountryListAdapter(context)
        recyclerView.adapter = adapter

        initListeners()
        initObservers()

        val scale = requireContext().resources.displayMetrics.density
        recyclerView.cameraDistance = DISTANCE_MARGIN * scale
    }

    private fun initObservers() {
        countriesListViewModel.allCountries.observe(viewLifecycleOwner, Observer { countries ->
            // Update the cached copy of the words in the adapter.
            Log.d("TEST1", countries.toString())

            if (countries.isNotEmpty()) {
                countries?.let { adapter.setContries(it) }
            }
        })
        countriesListViewModel.global.observe(viewLifecycleOwner, Observer { global ->

            if (global.isNotEmpty()) {
                casesTV.text = global[0].totalConfirmed.formatToStringWithDiv()
                deathsTV.text = global[0].totalDeaths.formatToStringWithDiv()
                recoveredTV.text = global[0].totalRecovered.formatToStringWithDiv()
            }
            Log.d("TEST2", global.toString())
        })
        countriesListViewModel.errors.observe(viewLifecycleOwner, Observer { error ->
            when (error) {
                "success" -> Toast.makeText(
                    context,
                    getString(R.string.update_success),
                    Toast.LENGTH_LONG
                )
                    .show()
                else -> Toast.makeText(
                    context,
                    getString(R.string.internet_error) + error,
                    Toast.LENGTH_LONG
                )
                    .show()
            }

        })
    }

    private fun initListeners() {

        adapter.onLongItemClickListener = object : CountryListAdapter.OnLongItemClickListener {

            override fun onLongItemClick(id: Int) {
                Log.d("TEST-ID", id.toString())
                parentFragmentManager.beginTransaction()
                    .setCustomAnimations(R.animator.back_animator, R.animator.front_animator)
                    .replace(
                        R.id.frame_container, DetailCountryScreenFragment.newInstance(
                            id
                        )
                    ).addToBackStack(MAIN_SCREEN_FRAGMENT_TAG)
                    .commit()
            }
        }
        spinner_with_sort_items.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    itemSelected: View?,
                    selectedItemPosition: Int,
                    selectedId: Long
                ) {
                    Log.d("TEST_ITEM", selectedItemPosition.toString())
                    when (selectedItemPosition) {
                        0 -> countriesListViewModel.getSortedCountries(SORTED_BY_ALPHABET)
                        1 -> countriesListViewModel.getSortedCountries(SORTED_BY_TOTAL_CONFIRMED)
                        2 -> countriesListViewModel.getSortedCountries(SORTED_BY_TOTAL_DEATHS)
                        else -> countriesListViewModel.getSortedCountries(SORTED_BY_TOTAL_RECOVERED)
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    return
                }
            }
        searchSV.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                Log.d("TEST", "SEARCH_CLicked")
                adapter.filter.filter(newText)
                return false
            }
        })
        updateIB.setOnClickListener {
            countriesListViewModel.loadData()
        }
    }

    companion object {
        private const val KEY_SPINNER_ITEM_POSITION = "KEY_SPINNER_ITEM_POSITION"
        private const val DISTANCE_MARGIN = 8000
        private const val MAIN_SCREEN_FRAGMENT_TAG = "MAIN_SCREEN_FRAGMENT_TAG"
        private const val SORTED_BY_ALPHABET = "id ASC"
        private const val SORTED_BY_TOTAL_CONFIRMED = "totalConfirmed DESC"
        private const val SORTED_BY_TOTAL_DEATHS = "totalDeaths DESC"
        private const val SORTED_BY_TOTAL_RECOVERED = "totalRecovered DESC"
    }
}