package com.ademozalp.kotlincountry.view

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.ademozalp.kotlincountry.databinding.FragmentCountryBinding
import com.ademozalp.kotlincountry.util.downloadFromUrl
import com.ademozalp.kotlincountry.util.placeholderProgressBar
import com.ademozalp.kotlincountry.viewmodel.CountryViewModel

class CountryFragment : Fragment() {
    private lateinit var binding : FragmentCountryBinding
    private lateinit var countryViewModel : CountryViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentCountryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val uuid = arguments?.getInt("countryid")

        countryViewModel = ViewModelProviders.of(this).get(CountryViewModel :: class.java)
        uuid?.let {
            countryViewModel.getDataFromRoom(it)
        }

        observeData(view.context)
    }

    private fun observeData(context: Context){
        countryViewModel.countryLiveData.observe(viewLifecycleOwner){ country ->
            binding.countryName.text = country.name
            binding.countryCapital.text = country.capital
            binding.countryRegion.text = country.region
            binding.countryCurrency.text = country.currency
            binding.countryLanguage.text = country.language
            binding.countryImg.downloadFromUrl(country.flag.toString(), placeholderProgressBar(context))
        }
    }
}