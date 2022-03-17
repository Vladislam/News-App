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
import com.example.news.util.extensions.launchViewLifecycleScope
import com.example.news.util.extensions.throttleClicks
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
            buttonChangeCountry.throttleClicks(lifecycleScope) {
                createChangingCountryDialog()
            }

            buttonChangeDarkTheme.throttleClicks(lifecycleScope) {
                val action =
                    SettingsFragmentDirections.actionSettingsFragmentToThemesChoiceFragment()
                findNavController().navigate(action)
            }
        }
    }

    private fun createChangingCountryDialog() {
        val countryMapper = CountryMapper(resources)

        val (container, spinnerCountries) = createExposedDropdownMenu(countryMapper)

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

    private fun createExposedDropdownMenu(countryMapper: CountryMapper): Pair<View, AutoCompleteTextView> {
        val container = FrameLayout(requireActivity())
        val params = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        ).apply {
            marginStart = resources.getDimensionPixelSize(R.dimen.dialog_margin)
            marginEnd = resources.getDimensionPixelSize(R.dimen.dialog_margin)
        }
        val input = layoutInflater.inflate(R.layout.spinner_view_item, container, false)
            .apply { layoutParams = params }
        val spinnerCountries =
            input.findViewById<AutoCompleteTextView>(R.id.spinnerCountries).apply {
                setAdapter(
                    ArrayAdapter(
                        requireContext(),
                        android.R.layout.simple_spinner_dropdown_item,
                        countryMapper.countryNames,
                    )
                )
            }
        container.addView(input)
        launchViewLifecycleScope {
            viewModel.preferencesFlow.collect { preferences ->
                spinnerCountries.setText(countryMapper.mapCodeToName(preferences.countryCode))
            }
        }

        return Pair(container, spinnerCountries)
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