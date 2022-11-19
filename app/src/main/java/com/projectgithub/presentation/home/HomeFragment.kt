package com.projectgithub.presentation.home

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import com.projectgithub.R
import com.projectgithub.common.Resources
import com.projectgithub.data.Repository
import com.projectgithub.data.network.ApiConfig
import com.projectgithub.databinding.FragmentHomeBinding
import com.projectgithub.presentation.home.adapter.HomeAdapter

class HomeFragment : Fragment(R.layout.fragment_home) {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var homeAdapter: HomeAdapter
    private lateinit var homeViewModel: HomeViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)

        initRecycler()
        initObserver()
        setupSearch()
    }

    private fun setupSearch() {
        binding.apply {
            svUserList.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    query?.let {
                        if (it.isNotEmpty()) {
                            homeViewModel.searchUser(query)
                            svUserList.clearFocus()
                        }
                    }
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    newText?.let {
                        if (it.isNotEmpty()) {
                            homeViewModel.searchUser(newText)
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
        val repository = Repository(ApiConfig.apiServices)
        homeViewModel = HomeViewModel(repository)
        homeViewModel.state.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resources.Loading -> {
                    binding.pbHome.visibility = View.VISIBLE
                    binding.rvUserList.visibility = View.INVISIBLE
                }
                is Resources.Success -> {
                    binding.pbHome.visibility = View.INVISIBLE
                    binding.rvUserList.visibility = View.VISIBLE
                    response.data?.let { homeAdapter.setData(it) }
                }
                is Resources.Error -> {
                    binding.pbHome.visibility = View.INVISIBLE
                    binding.rvUserList.visibility = View.INVISIBLE
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