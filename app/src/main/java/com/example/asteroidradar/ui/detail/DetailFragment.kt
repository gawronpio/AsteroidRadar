package com.example.asteroidradar.ui.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.example.asteroidradar.R
import com.example.asteroidradar.databinding.FragmentDetailBinding
import com.example.asteroidradar.network_database.database.AsteroidDatabase

class DetailFragment : Fragment() {
    private lateinit var binding: FragmentDetailBinding
    private val arguments by lazy { DetailFragmentArgs.fromBundle(requireArguments()) }
    private val databaseDao by lazy { AsteroidDatabase.getDatabase(application).asteroidDatabaseDao }
    private val application by lazy { requireNotNull(this.activity).application }
    private val viewModel: DetailViewModel by viewModels {
        DetailViewModelFactory(arguments.asteroidId, databaseDao, application)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }
}