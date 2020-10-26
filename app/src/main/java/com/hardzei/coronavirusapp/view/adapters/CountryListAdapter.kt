package com.hardzei.coronavirusapp.view.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.hardzei.coronavirusapp.R
import com.hardzei.coronavirusapp.data.entity.coronastatistic.Country
import com.hardzei.coronavirusapp.view.formatToStringWithDiv
import kotlinx.android.synthetic.main.list_item_for_country.view.*


class CountryListAdapter(
    context: Context?
) : RecyclerView.Adapter<CountryListAdapter.CountryViewHolder>(), Filterable {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var countries = mutableListOf<Country>()
    private var countriesAll = mutableListOf<Country>()
    var onLongItemClickListener: OnLongItemClickListener? = null

    fun setContries(countries: List<Country>) {
        this.countries = countries.toMutableList()
        this.countriesAll = countries.toMutableList()
        notifyDataSetChanged()
    }

    interface OnLongItemClickListener {
        fun onLongItemClick(id: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        val view = inflater.inflate(R.layout.list_item_for_country, parent, false)
        return CountryViewHolder(view)
    }

    override fun getItemCount() = countries.size

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        with(holder) {
            countryName.text = countries[position].country
            cases.text = countries[position].totalConfirmed.formatToStringWithDiv()
            deaths.text = countries[position].totalDeaths.formatToStringWithDiv()
            recovered.text = countries[position].totalRecovered.formatToStringWithDiv()
            itemView.setOnLongClickListener {
                onLongItemClickListener?.onLongItemClick(countries[position].id)
                true
            }
        }
    }

    private val filter = object : Filter() {

        // run on background thread
        override fun performFiltering(constraint: CharSequence?): FilterResults {

            val filteredCountries = mutableListOf<Country>()

            Log.d("TEST", "CLICK_ADAPTER $constraint")
            if (constraint.isNullOrBlank()) {
                filteredCountries.addAll(countriesAll)
            } else {
                for (country in countriesAll) {
                    if (country
                            .country
                            .toLowerCase()
                            .contains(
                                constraint
                                    .toString()
                                    .toLowerCase()
                            )
                    ) {
                        filteredCountries.add(country)
                    }
                }
            }
            Log.d("TEST", "CLICK_ADAPTER ${filteredCountries.size}")
            val filterResults = FilterResults()
            filterResults.values = filteredCountries

            return filterResults
        }

        // run on ui thread
        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            countries.clear()
            countries.addAll(results?.values as Collection<Country>)
            notifyDataSetChanged()
        }
    }

    override fun getFilter(): Filter {
        return filter
    }

    class CountryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val countryName = itemView.countryNameTV
        val cases = itemView.casesTV
        val deaths = itemView.deathsTV
        val recovered = itemView.recoveredTV
    }
}