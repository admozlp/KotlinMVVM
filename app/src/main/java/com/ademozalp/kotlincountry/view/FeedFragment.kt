package com.ademozalp.kotlincountry.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.ademozalp.kotlincountry.adapter.FeedAdapter
import com.ademozalp.kotlincountry.databinding.FragmentFeedBinding
import com.ademozalp.kotlincountry.viewmodel.FeedViewModel

class FeedFragment : Fragment() {
    private lateinit var binding : FragmentFeedBinding
    private lateinit var viewModel : FeedViewModel
    private val countryAdapter = FeedAdapter(arrayListOf())

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentFeedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(FeedViewModel :: class.java)
        viewModel.refreshData()

        binding.countryList.layoutManager = LinearLayoutManager(requireContext())
        binding.countryList.adapter = countryAdapter

        observeLiveData()

        binding.swipeRefresh.setOnRefreshListener {
            binding.errortext.visibility = View.GONE
            binding.countryList.visibility = View.GONE
            binding.countryLoading.visibility = View.VISIBLE
            viewModel.refreshFromApi()
            binding.swipeRefresh.isRefreshing = false
        }
    }

    private fun observeLiveData(){

        viewModel.countries.observe(viewLifecycleOwner ){countries ->
            countries?.let {
                binding.countryList.visibility = View.VISIBLE
                countryAdapter.updateCountryList(ArrayList(it))
            }
        }

        viewModel.countryError.observe(viewLifecycleOwner) {
            if(it) {
                //hata var
                binding.errortext.visibility = View.VISIBLE
                viewModel.errorText.observe(viewLifecycleOwner){e->
                    binding.errortext.text = e
                }
            } else {
                //hata yok
                binding.errortext.visibility = View.GONE
            }
        }

        viewModel.countryLoading.observe(viewLifecycleOwner) {
            if (it) {
                //yükleniyor
                binding.countryLoading.visibility = View.VISIBLE
                binding.countryList.visibility = View.GONE
                binding.errortext.visibility = View.GONE
            } else {
                binding.countryLoading.visibility = View.GONE
            }
        }


    }
}