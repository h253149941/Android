package com.example.xhh.ui.weather

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.xhh.R
import kotlinx.android.synthetic.main.fragment_weather.*


class WeatherFragment : Fragment() {

    lateinit var notificationsViewModel: WeatherViewModel
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_weather, container, false)
        }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        notificationsViewModel = ViewModelProviders.of(this).get(WeatherViewModel::class.java)

        notificationsViewModel.cities.observe(viewLifecycleOwner, Observer {
            val cities = it
            val adapter = getActivity()?.let { it1 ->
                ArrayAdapter<City>(
                    it1,
                    android.R.layout.simple_list_item_1,
                    cities
                )
            }
            listView.adapter = adapter
            listView.setOnItemClickListener { _, _, position, _ ->
                val cityCode = cities[position].city_code
                val intent = Intent(getActivity(),Weather_MainActivity2::class.java)
                intent.putExtra("city_code", cityCode)
                startActivity(intent)
            }
        })
    }

}