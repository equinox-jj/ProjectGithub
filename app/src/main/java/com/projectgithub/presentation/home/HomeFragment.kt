package com.projectgithub.presentation.home

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import com.projectgithub.R
import com.projectgithub.common.Resources
import com.projectgithub.common.setVisibilityGone
import com.projectgithub.common.setVisibilityVisible
import com.projectgithub.databinding.FragmentHomeBinding
import com.projectgithub.presentation.factory.ViewModelFactory
import com.projectgithub.presentation.home.adapter.HomeAdapter

class HomeFragment : Fragment(R.layout.fragment_home) {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var homeAdapter: HomeAdapter
    private val homeViewModel by viewModels<HomeViewModel> { ViewModelFactory.getInstance() }
    private var currentQuery = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)

        initRecycler()
        initObserver()
        setupSearch()
    }

    private fun setupSearch() {
        binding.apply {
            svUserList.isSubmitButtonEnabled = true
            svUserList.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    query?.let {
                        if (it.isNotEmpty()) {
                            currentQuery = it
                            homeViewModel.searchUser(it)
                            svUserList.clearFocus()
                        }
                    }
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    newText?.let {
                        if (it.isNotEmpty()) {
                            currentQuery = it
                            homeViewModel.searchUser(it)
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

    private fun initObserver() {
        homeViewModel.state.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resources.Loading -> {
                    isLoadingState(true)
                    isUserNotFound(false)
                    isError(false)
                }
                is Resources.Success -> {
                    isLoadingState(false)
                    isError(false)
                    if (response.data?.isNotEmpty() == true) {
                        isUserNotFound(false)
                        homeAdapter.setData(response.data)
                    } else {
                        isUserNotFound(true)
                        Toast.makeText(
                            context,
                            response.message ?: getString(R.string.user_not_found),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                is Resources.Error -> {
                    isLoadingState(false)
                    isUserNotFound(false)
                    isError(true)
                    showErrorSnackBar()
                }
            }
        }
    }

    private fun isLoadingState(isLoading: Boolean) {
        if (isLoading) {
            binding.pbHome.setVisibilityVisible()
            binding.rvUserList.setVisibilityGone()
            binding.ivSearchPerson.setVisibilityGone()
            binding.tvSearchText.setVisibilityGone()
        } else {
            binding.pbHome.setVisibilityGone()
            binding.rvUserList.setVisibilityVisible()
        }
    }

    private fun isUserNotFound(isNotFound: Boolean) {
        if (isNotFound) {
            binding.rvUserList.setVisibilityGone()
            binding.ivSearchPerson.setVisibilityVisible()
            binding.tvSearchText.setVisibilityVisible()
        } else {
            binding.ivSearchPerson.setVisibilityGone()
            binding.tvSearchText.setVisibilityGone()
        }
    }

    private fun isError(isError: Boolean) {
        if (isError) {
            binding.rvUserList.setVisibilityGone()
            binding.lottieHome.setVisibilityVisible()
            binding.tvErrorHome.setVisibilityVisible()
        } else {
            binding.lottieHome.setVisibilityGone()
            binding.tvErrorHome.setVisibilityGone()
        }
    }

    private fun initRecycler() {
        binding.apply {
            homeAdapter = HomeAdapter()
            rvUserList.adapter = homeAdapter
            rvUserList.setHasFixedSize(true)
        }
    }

    private fun showErrorSnackBar() {
        Snackbar.make(
            binding.constraintHome,
            getString(R.string.error_when_load_the_data),
            Snackbar.LENGTH_INDEFINITE
        )
            .setAction(getString(R.string.retry)) {
                homeViewModel.onRefresh(currentQuery)
            }
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}