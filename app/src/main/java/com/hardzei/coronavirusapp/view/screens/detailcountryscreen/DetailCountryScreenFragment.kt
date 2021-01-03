package com.hardzei.coronavirusapp.view.screens.detailcountryscreen

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.hardzei.coronavirusapp.PARAM_COUNTRY_ID
import com.hardzei.coronavirusapp.R
import com.hardzei.coronavirusapp.SUCCESS_STATUS
import com.hardzei.coronavirusapp.view.formatToStringWithDiv
import com.hardzei.coronavirusapp.viewmodel.DetailCountryViewModel
import kotlinx.android.synthetic.main.fragment_detail_screen.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailCountryScreenFragment : Fragment() {
    private var countryId: Int = 0
    private lateinit var navController: NavController
    private val detailCountryViewModel by viewModel<DetailCountryViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            countryId = it.getInt(PARAM_COUNTRY_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(
            R.layout.fragment_detail_screen,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

        initObservers()

        Log.d("TEST-ID2", countryId.toString())

        detailCountryViewModel.getCountryById(countryId)
    }

    private fun initObservers() {
        // detail info about our country
        detailCountryViewModel.countryDetail.observe(
            viewLifecycleOwner,
            Observer { country ->

                if (country != null) {

                    Glide.with(this)
                        .load("http://www.geognos.com/api/en/countries/flag/${country.countryCode}.png")
                        .transition(DrawableTransitionOptions.withCrossFade(DELAY_FOR_ANIMATION))
                        .into(flagImageView)

                    nameTV.text = country.country

                    casesTV.text = country.totalConfirmed.formatToStringWithDiv()
                    newCasesTV.text = country.newConfirmed.formatToStringWithDiv()

                    deathsTV.text = country.totalDeaths.formatToStringWithDiv()
                    newDeathsTV.text = country.newDeaths.formatToStringWithDiv()

                    recoveredTV.text = country.totalRecovered.formatToStringWithDiv()
                    newRecoveredTV.text = country.newRecovered.formatToStringWithDiv()

                    updatedTV.text = country.date.replace("T", " ").replace("Z", " ")
                }
            }
        )
        // link with url our image
        detailCountryViewModel.countryLink.observe(
            viewLifecycleOwner,
            Observer {

                Log.d("TEST-LINK_DetFrag", it)

                Glide.with(this)
                    .load(it)
                    .transition(DrawableTransitionOptions.withCrossFade(DELAY_FOR_ANIMATION))
                    .into(detailIBackgroundIV)
            }
        )
        detailCountryViewModel.errors.observe(
            viewLifecycleOwner,
            Observer { error ->
                if (error != SUCCESS_STATUS) {
                    Toast.makeText(
                        context,
                        getString(R.string.internet_error) + error,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        )
    }

    override fun onPause() {
        super.onPause()
        detailCountryViewModel.stopShowing()
    }

    private companion object {
        const val DELAY_FOR_ANIMATION = 500
    }
}
