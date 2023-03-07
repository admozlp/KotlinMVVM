package com.ademozalp.kotlincountry.adapter

import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import com.ademozalp.kotlincountry.model.Country
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.ademozalp.kotlincountry.R
import com.ademozalp.kotlincountry.util.downloadFromUrl
import com.ademozalp.kotlincountry.util.placeholderProgressBar
import com.ademozalp.kotlincountry.view.FeedFragmentDirections
import kotlinx.android.synthetic.main.item_country.view.*

class FeedAdapter(var countryList : ArrayList<Country>) : RecyclerView.Adapter<FeedAdapter.FeedHolder>() {


    class FeedHolder(var view : View) : RecyclerView.ViewHolder(view) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_country, parent, false)
        return FeedHolder(view)
    }

    override fun onBindViewHolder(holder: FeedHolder, position: Int) {
        holder.view.name.text = countryList[position].name
        holder.view.region.text = countryList[position].region
        holder.view.countryImage.downloadFromUrl(countryList[position].flag.toString(), placeholderProgressBar(holder.view.context))

        holder.view.setOnClickListener{
            val action = FeedFragmentDirections.actionFeedFragmentToCountryFragment(countryList[position].uuid)
            Navigation.findNavController(it).navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return countryList.size
    }

    fun updateCountryList(newCountryList : List<Country>){
        countryList.clear()
        countryList.addAll(newCountryList)
        notifyDataSetChanged()
    }
}