package com.example.asteroidradar.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuProvider
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.asteroidradar.R
import com.example.asteroidradar.databinding.FragmentMainBinding
import com.google.android.material.snackbar.Snackbar


class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding

    private val menuProvider = object : MenuProvider {
        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            menuInflater.inflate(R.menu.filtering, menu)
        }

        override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
            when (menuItem.itemId) {
                R.id.filterNext7Days -> {
                    viewModel.showNext7Days()
                    return true
                }
                R.id.filterToday -> {
                    viewModel.showToday()
                    return true
                }
                R.id.filterAllAsteroids -> {
                    viewModel.showAll()
                    return true
                }
            }
            return false
        }
    }

    private val application by lazy { requireNotNull(this.activity).application }
    private val viewModel: MainViewModel by viewModels {
        MainViewModel.Factory(application)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        requireActivity().addMenuProvider(menuProvider, viewLifecycleOwner)

        viewModel.apodUrl.observe(viewLifecycleOwner) { url ->
            loadPicture(url)
        }
        viewModel.missingApod.observe(viewLifecycleOwner) { missing ->
            if(missing) {
                binding.imageOfTheDay.setImageResource(R.drawable.baseline_broken_image_24)
            }
        }

        val manager = LinearLayoutManager(activity)
        binding.asteroidRecycler.layoutManager = manager
        val adapter = AsteroidRecyclerAdapter(AsteroidRecyclerListener {
            asteroidId -> viewModel.onAsteroidClicked(asteroidId)
        })
        binding.asteroidRecycler.adapter = adapter

        viewModel.navigateToAsteroidDetail.observe(viewLifecycleOwner) { asteroidId ->
            asteroidId?.let {
                findNavController().navigate(MainFragmentDirections.actionMainFragmentToDetailFragment(asteroidId))
                viewModel.onAsteroidDetailNavigated()
            }
        }
        viewModel.asteroidsData.observe(viewLifecycleOwner) {
            it?.let {
                adapter.addAndSubmitList(it)
            }
        }

        viewModel.httpError.observe(viewLifecycleOwner) {
            if(it) {
                showHttpErrorInfo()
                viewModel.onHttpErrorRead()
            }
        }

        return binding.root
    }

    private fun showHttpErrorInfo() {
        Snackbar.make(binding.root, getString(R.string.internet_not_ok), Snackbar.LENGTH_INDEFINITE)
            .setAction("OK") {}
            .show()
    }

    private fun loadPicture(url: String) {
        val imageView = binding.imageOfTheDay
        Glide.with(this)
            .load(url)
            .placeholder(R.drawable.placeholder_picture_of_day)
            .error(R.drawable.baseline_broken_image_24)
            .into(imageView)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        requireActivity().removeMenuProvider(menuProvider)
    }
}