package com.projectgithub.presentation.settings

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.projectgithub.R
import com.projectgithub.databinding.FragmentSettingsBinding
import com.projectgithub.presentation.factory.ViewModelFactory

class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private val settingsViewModel by activityViewModels<SettingsViewModel> { ViewModelFactory.getInstance(requireContext()) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSettingsBinding.bind(view)

        initObserver()
        setupToolbar()
    }

    private fun setupToolbar() {
        binding.toolbarSettings.apply {
            setNavigationIcon(R.drawable.ic_baseline_arrow_back_ios_new_24)
            setupWithNavController(findNavController())
        }
    }

    private fun initObserver() {
        settingsViewModel.getDarkModeKey.observe(viewLifecycleOwner) { isDarkMode ->
            if (isDarkMode) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                binding.switchSetting.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                binding.switchSetting.isChecked = false
            }
        }
        binding.switchSetting.setOnCheckedChangeListener { _, isChecked ->
            settingsViewModel.saveDarkModeKey(isChecked)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}