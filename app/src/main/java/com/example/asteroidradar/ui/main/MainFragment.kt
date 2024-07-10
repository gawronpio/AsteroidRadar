package com.example.asteroidradar.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.example.asteroidradar.R
import com.example.asteroidradar.databinding.FragmentMainBinding
import com.example.asteroidradar.network_database.database.AsteroidDatabase


class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding

    private val application by lazy { requireNotNull(this.activity).application }
    private val dataSource by lazy { AsteroidDatabase.getDatabase(application).asteroidDatabaseDao }
    private val viewModel: MainViewModel by viewModels {
        MainViewModel.Factory(dataSource, application)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel

        return binding.root
    }
}