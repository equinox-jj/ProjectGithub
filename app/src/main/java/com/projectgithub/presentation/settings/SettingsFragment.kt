package com.projectgithub.presentation.settings

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
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
    }

    private fun initObserver() {
        settingsViewModel.getDarkModeKey.observe(viewLifecycleOwner) { isDarkMode ->
            if (isDarkMode) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                binding.ivSettings.setImageResource(R.drawable.crescent_moon)
                binding.switchSetting.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                binding.ivSettings.setImageResource(R.drawable.sun)
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