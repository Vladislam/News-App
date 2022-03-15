package com.example.news.ui.fragments

import android.content.Context
import android.os.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.FrameLayout
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.news.R
import com.example.news.databinding.FragmentSettingsBinding
import com.example.news.ui.fragments.base.BaseFragment
import com.example.news.util.mappers.CountryMapper
import com.example.news.viewmodels.SettingsViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SettingsFragment : BaseFragment(R.layout.fragment_settings) {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SettingsViewModel by viewModels()

    private val countryMapper by lazy { CountryMapper(resources) }

    override fun setup(savedInstanceState: Bundle?) {
        setupViewModelCallbacks()
        setupButtons()
    }

    private fun setupViewModelCallbacks() {
        lifecycleScope.launch {
            viewModel.errorFlow
                .map { it.message }
                .collect { msg ->
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        (requireActivity().getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager).defaultVibrator.vibrate(
                            VibrationEffect.createOneShot(200, 1)
                        )
                    } else { // Build below sdk 31
                        @Suppress("DEPRECATION")
                        (requireActivity().getSystemService(Context.VIBRATOR_SERVICE) as Vibrator).vibrate(
                            200L
                        )
                    }
                    Snackbar.make(requireView(), msg, Snackbar.LENGTH_LONG).show()
                }
        }
    }

    private fun setupButtons() {
        binding.apply {
            buttonChangeCountry.setOnClickListener {
                createChangingCountryDialog()
            }

            buttonChangeDarkTheme.setOnClickListener {
                val action = SettingsFragmentDirections.actionSettingsFragmentToThemesChoiceFragment()
                findNavController().navigate(action)
            }
        }
    }

    private fun createChangingCountryDialog() {
        val container = FrameLayout(requireActivity())
        val params = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        params.marginStart = resources.getDimensionPixelSize(R.dimen.dialog_margin)
        params.marginEnd = resources.getDimensionPixelSize(R.dimen.dialog_margin)
        val input = layoutInflater.inflate(R.layout.spinner_view_item, container, false)
        val spinnerCountries =
            input.findViewById<AutoCompleteTextView>(R.id.spinnerCountries)
        spinnerCountries.setAdapter(
            ArrayAdapter(
                requireContext(),
                R.layout.spinner_item,
                countryMapper.countryNames,
            )
        )
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.preferencesFlow.collect { preferences ->
                spinnerCountries.setText(countryMapper.mapCodeToName(preferences.countryCode))
            }
        }
        input.layoutParams = params
        container.addView(input)

        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.enter_new_country))
            .setView(container)
            .setPositiveButton(getString(R.string.apply)) { _, _ ->
                viewModel.onSaveCountryCodeClick(
                    countryMapper.mapNameToCode(spinnerCountries.text.toString())
                )
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .create()
            .show()

    }

    override fun setupBinding(inflater: LayoutInflater, container: ViewGroup?): View {
        _binding = FragmentSettingsBinding.inflate(inflater)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}