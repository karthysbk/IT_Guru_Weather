package com.app.itguruweather.ui

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.onNavDestinationSelected
import com.app.itguruweather.R
import com.app.itguruweather.databinding.LoginScreenBinding
import com.app.itguruweather.databinding.WeatherScreenBinding
import com.app.itguruweather.viewmodel.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class WeatherFragment : Fragment() {

    private lateinit var binding: WeatherScreenBinding
    private lateinit var viewModel: WeatherViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(WeatherViewModel::class.java)
        binding = WeatherScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        lifecycleScope.launchWhenCreated {
            viewModel.weatherUiState.collect { uiState ->
                when (uiState) {
                    is WeatherViewModel.WeatherUiState.Weather -> {
                        binding.apply {
                            tvHumidity.text = uiState.humidity
                            tvTemp.text = uiState.temp
                            tvWeatherType.text = uiState.weatherType
                            tvWindSpeed.text = uiState.windSpeed
                        }
                        binding.pbWeather.visibility = View.INVISIBLE
                    }
                    is WeatherViewModel.WeatherUiState.Failure -> {
                        binding.pbWeather.visibility = View.INVISIBLE
                        binding.apply {
                            tvHumidity.text = uiState.message
                            tvTemp.text = uiState.message
                            tvWeatherType.text = uiState.message
                            tvWindSpeed.text = uiState.message
                        }
                    }
                    is WeatherViewModel.WeatherUiState.Loading -> {
                        binding.pbWeather.visibility = View.VISIBLE
                    }
                    else -> Unit
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.log_out_menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.log_out_user){
            findNavController().navigate(R.id.action_weatherFragment_to_welcomeFragment)
        }
        return item.onNavDestinationSelected(findNavController()) || super.onOptionsItemSelected(item)
    }

}