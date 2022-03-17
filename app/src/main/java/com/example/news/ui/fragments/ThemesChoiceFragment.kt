package com.example.news.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.viewModels
import com.example.news.R
import com.example.news.databinding.FragmentThemesChoiceBinding
import com.example.news.ui.fragments.base.BaseFragment
import com.example.news.util.extensions.launchViewLifecycleScope
import com.example.news.viewmodels.SettingsViewModel

class ThemesChoiceFragment : BaseFragment(R.layout.fragment_themes_choice) {

    private var _binding: FragmentThemesChoiceBinding? = null
    private val binding
        get() = _binding!!

    private val viewModel: SettingsViewModel by viewModels()

    override fun setup(savedInstanceState: Bundle?) {
        setupViewModelCallbacks()
        setupRadioButtons()
    }

    private fun setupViewModelCallbacks() {
        launchViewLifecycleScope {
            viewModel.preferencesFlow.collect { preferences ->
                when (preferences.appTheme) {
                    AppCompatDelegate.MODE_NIGHT_YES -> {
                        binding.rbDark.isChecked = true
                    }
                    AppCompatDelegate.MODE_NIGHT_NO -> {
                        binding.rbLight.isChecked = true
                    }
                    AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM -> {
                        binding.rbDefault.isChecked = true
                    }
                }
            }
        }
    }

    private fun setupRadioButtons() {
        binding.radioGroup.setOnCheckedChangeListener { _, id ->
            viewModel.onSwitchDarkThemeClick(
                when (id) {
                    R.id.rbDark -> {
                        AppCompatDelegate.MODE_NIGHT_YES
                    }
                    R.id.rbLight -> AppCompatDelegate.MODE_NIGHT_NO
                    else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                }
            )
        }
    }

    override fun setupBinding(inflater: LayoutInflater, container: ViewGroup?): View {
        _binding = FragmentThemesChoiceBinding.inflate(inflater)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}