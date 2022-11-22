package com.projectgithub.presentation.home

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import com.projectgithub.R
import com.projectgithub.common.Constants
import com.projectgithub.common.Resources
import com.projectgithub.data.preferences.ThemeDataStore
import com.projectgithub.data.repository.RemoteRepository
import com.projectgithub.data.source.remote.network.ApiConfig
import com.projectgithub.databinding.FragmentHomeBinding
import com.projectgithub.presentation.factory.RemoteVMFactory
import com.projectgithub.presentation.home.adapter.HomeAdapter
import com.projectgithub.presentation.theme.ThemeViewModel
import com.projectgithub.presentation.theme.ThemeViewModelFactory

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(Constants.PREF_NAME)

class HomeFragment : Fragment(R.layout.fragment_home) {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var homeAdapter: HomeAdapter
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var themeViewModel: ThemeViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)

        initRecycler()
        initObserver()
        initDarkMode()
        setupSearch()
        setupMenu()
    }

    private fun setupMenu() {
        binding.toolbarHome.apply {
            val menuHost: MenuHost = this@apply
            menuHost.addMenuProvider(object : MenuProvider {
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    menuInflater.inflate(R.menu.menu_home, menu)
                }

                override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                    when (menuItem.itemId) {
                        R.id.dark_mode_menu -> {

                        }
                    }
                    return true
                }
            }, viewLifecycleOwner, Lifecycle.State.RESUMED)
        }
    }

    private fun initDarkMode() {
        val themeDataStore = ThemeDataStore.getInstance(requireContext().dataStore)
        val themeFactory = ThemeViewModelFactory(themeDataStore)
        themeViewModel = ViewModelProvider(this, themeFactory)[ThemeViewModel::class.java]

        themeViewModel.getDarkModeKey.observe(viewLifecycleOwner) {

        }
    }

    private fun setupSearch() {
        binding.apply {
            svUserList.isSubmitButtonEnabled = true
            svUserList.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    query.let {
                        if (it.isNotEmpty()) {
                            homeViewModel.searchUser(query)
                            setupOnRefresh(query)
                            svUserList.clearFocus()
                        }
                    }
                    return true
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    newText.let {
                        if (it.isNotEmpty()) {
                            homeViewModel.searchUser(newText)
                            setupOnRefresh(newText)
                        }
                    }
                    return true
                }
            })
            svUserList.setOnCloseListener {
                svUserList.setQuery("", false)
                svUserList.clearFocus()
                true
            }
        }
    }

    private fun setupOnRefresh(query: String) {
        binding.apply {
            refreshHome.setOnRefreshListener {
                homeViewModel.onRefresh(query)
                refreshHome.isRefreshing = false
            }
        }
    }

    private fun initObserver() {
        val remoteRepository = RemoteRepository(ApiConfig.apiServices)
        val factory = RemoteVMFactory(remoteRepository)
        homeViewModel = ViewModelProvider(this, factory)[HomeViewModel::class.java]
        homeViewModel.state.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resources.Loading -> {
                    binding.pbHome.visibility = View.VISIBLE
                    binding.rvUserList.visibility = View.INVISIBLE
                    binding.ivSearchPerson.visibility = View.INVISIBLE
                    binding.tvSearchText.visibility = View.INVISIBLE
                    binding.lottieHome.visibility = View.INVISIBLE
                    binding.tvErrorHome.visibility = View.INVISIBLE
                }
                is Resources.Success -> {
                    response.data?.let {
                        if (it.isNotEmpty()) {
                            binding.pbHome.visibility = View.INVISIBLE
                            binding.rvUserList.visibility = View.VISIBLE
                            binding.ivSearchPerson.visibility = View.INVISIBLE
                            binding.tvSearchText.visibility = View.INVISIBLE
                            binding.lottieHome.visibility = View.INVISIBLE
                            binding.tvErrorHome.visibility = View.INVISIBLE
                            homeAdapter.setData(it)
                        } else {
                            binding.pbHome.visibility = View.INVISIBLE
                            binding.rvUserList.visibility = View.INVISIBLE
                            binding.ivSearchPerson.visibility = View.VISIBLE
                            binding.tvSearchText.visibility = View.VISIBLE
                            binding.lottieHome.visibility = View.INVISIBLE
                            binding.tvErrorHome.visibility = View.INVISIBLE
                            Toast.makeText(
                                context,
                                response.message ?: "User Not Found.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
                is Resources.Error -> {
                    binding.pbHome.visibility = View.INVISIBLE
                    binding.rvUserList.visibility = View.INVISIBLE
                    binding.ivSearchPerson.visibility = View.INVISIBLE
                    binding.tvSearchText.visibility = View.INVISIBLE
                    binding.lottieHome.visibility = View.VISIBLE
                    binding.tvErrorHome.visibility = View.VISIBLE
                    Toast.makeText(
                        context,
                        response.message ?: "Check Your Internet Connection.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun initRecycler() {
        binding.apply {
            homeAdapter = HomeAdapter()
            rvUserList.adapter = homeAdapter
            rvUserList.setHasFixedSize(true)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}