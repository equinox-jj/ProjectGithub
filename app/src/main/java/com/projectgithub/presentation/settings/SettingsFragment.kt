package com.projectgithub.presentation.settings

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.projectgithub.R
import com.projectgithub.common.Constants
import com.projectgithub.data.preferences.ThemeDataStore
import com.projectgithub.databinding.FragmentSettingsBinding
import com.projectgithub.presentation.theme.ThemeViewModel
import com.projectgithub.presentation.theme.ThemeViewModelFactory

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(Constants.PREF_NAME)

class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private lateinit var themeViewModel: ThemeViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSettingsBinding.bind(view)

        initObserver()
    }

    private fun initObserver() {
        val themeDataStore = ThemeDataStore.getInstance(requireContext().dataStore)
        val themeFactory = ThemeViewModelFactory(themeDataStore)
        themeViewModel = ViewModelProvider(this, themeFactory)[ThemeViewModel::class.java]
        themeViewModel.getDarkModeKey.observe(viewLifecycleOwner) { isDarkMode ->
            if (isDarkMode) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                binding.switchSetting.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                binding.switchSetting.isChecked = false
            }
        }
        binding.switchSetting.setOnCheckedChangeListener { _, isChecked ->
            themeViewModel.saveDarkModeKey(isChecked)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}