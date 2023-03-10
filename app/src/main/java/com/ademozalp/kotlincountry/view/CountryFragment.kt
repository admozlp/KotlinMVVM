package com.ademozalp.kotlincountry.view

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.ademozalp.kotlincountry.R
import com.ademozalp.kotlincountry.databinding.FragmentCountryBinding
import com.ademozalp.kotlincountry.viewmodel.CountryViewModel

class CountryFragment : Fragment() {
    //private lateinit var binding: FragmentCountryBinding
    private lateinit var countryViewModel: CountryViewModel
    private lateinit var dataBinding: FragmentCountryBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_country, container, false)
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val uuid = arguments?.getInt("countryid")

        countryViewModel = ViewModelProviders.of(this).get(CountryViewModel::class.java)

        uuid?.let {
            countryViewModel.getDataFromRoom(it)
        }

        observeData(view.context)
    }

    private fun observeData(context: Context) {

        countryViewModel.countryLiveData.observe(viewLifecycleOwner) { country ->
            country?.let {
                dataBinding.country = country

                /*
               binding.countryName.text = country.name
               binding.countryCapital.text = country.capital
               binding.countryRegion.text = country.region
               binding.countryCurrency.text = country.currency
               binding.countryLanguage.text = country.language
               binding.countryImg.downloadFromUrl(country.flag.toString(), placeholderProgressBar(context))
                */
            }
        }
    }
}

