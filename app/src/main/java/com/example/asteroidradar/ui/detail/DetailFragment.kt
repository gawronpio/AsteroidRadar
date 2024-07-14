package com.example.asteroidradar.ui.detail

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
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

        viewModel.showHelp.observe(viewLifecycleOwner) { showHelp ->
            if(showHelp) {
                showHelpDialog(requireContext())
                viewModel.onHelpShown()
            }
        }

        return binding.root
    }

    private fun showHelpDialog(context: Context) {
        val builder = AlertDialog.Builder(context)
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val dialogView = inflater.inflate(R.layout.help_dialog, null)
        val infoTextView = dialogView.findViewById<TextView>(R.id.helpTextView)
        infoTextView.text = getString(R.string.au_explanation)
        val acceptButton = dialogView.findViewById<Button>(R.id.acceptButton)
        val dialog = builder.setView(dialogView).create()
        acceptButton.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }
}