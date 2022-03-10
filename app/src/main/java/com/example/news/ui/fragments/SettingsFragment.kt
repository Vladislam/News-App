package com.example.news.ui.fragments

import android.content.Context
import android.os.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.news.R
import com.example.news.databinding.FragmentSettingsBinding
import com.example.news.ui.fragments.base.BaseFragment
import com.example.news.viewmodels.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SettingsFragment : BaseFragment(R.layout.fragment_settings) {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SettingsViewModel by viewModels()

    override fun setup(savedInstanceState: Bundle?) {
        setupViewModel()
        setupSpinner()
        setupDarkThemeSwitch()
    }

    private fun setupViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.preferencesFlow.collect { preferences ->
                binding.countryCode = preferences.countryCode.uppercase()
                binding.isDarkTheme = preferences.darkTheme
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
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
                    Toast.makeText(requireContext(), msg, Toast.LENGTH_LONG).show()
                }
        }
    }

    private fun setupDarkThemeSwitch() {
        binding.switchDarkTheme.setOnCheckedChangeListener { _, isChecked ->
            viewModel.onSwitchDarkThemeClick(isChecked)
        }
    }

    private fun setupSpinner() {
        val items = resources.getStringArray(R.array.country_codes).map { it.uppercase() }
        binding.apply {
            spinnerCountries.setAdapter(
                ArrayAdapter(
                    requireContext(),
                    R.layout.spinner_item,
                    items,
                )
            )
            btnSaveCountryCode.setOnClickListener {
                viewModel.onSaveCountryCodeClick(spinnerCountries.text.toString())
            }
        }
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