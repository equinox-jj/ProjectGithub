package com.projectgithub.presentation.followers

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.projectgithub.R
import com.projectgithub.common.Resources
import com.projectgithub.data.Repository
import com.projectgithub.data.source.remote.network.ApiConfig
import com.projectgithub.databinding.FragmentFollowersBinding
import com.projectgithub.presentation.ViewModelProviderFactory
import com.projectgithub.presentation.home.adapter.HomeAdapter

class FollowersFragment : Fragment(R.layout.fragment_followers) {

    private var _binding: FragmentFollowersBinding? = null
    private val binding get() = _binding!!

    private lateinit var followersAdapter: HomeAdapter
    private lateinit var followersViewModel: FollowersViewModel
    private lateinit var repository: Repository
    private lateinit var factory: ViewModelProviderFactory

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentFollowersBinding.bind(view)

        initObserver()
        initRecycler()
    }

    private fun initObserver() {
        val username = arguments?.getString("username").toString()

        repository = Repository(ApiConfig.apiServices)
        factory = ViewModelProviderFactory(repository)
        followersViewModel = ViewModelProvider(this, factory)[FollowersViewModel::class.java]

        setupOnRefresh(username)
        followersViewModel.getFollowers(username)
        followersViewModel.state.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resources.Loading -> {
                    binding.pbFollowers.visibility = View.VISIBLE
                    binding.rvFollowers.visibility = View.INVISIBLE
                    binding.tvErrorRetry.visibility = View.INVISIBLE
                    binding.btnErrorRetry.visibility = View.INVISIBLE
                }
                is Resources.Success -> {
                    binding.pbFollowers.visibility = View.INVISIBLE
                    binding.rvFollowers.visibility = View.VISIBLE
                    binding.tvErrorRetry.visibility = View.INVISIBLE
                    binding.btnErrorRetry.visibility = View.INVISIBLE
                    response.data?.let { followersAdapter.setData(it) }
                }
                is Resources.Error -> {
                    binding.pbFollowers.visibility = View.INVISIBLE
                    binding.rvFollowers.visibility = View.INVISIBLE
                    binding.tvErrorRetry.visibility = View.VISIBLE
                    binding.btnErrorRetry.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun initRecycler() {
        binding.apply {
            followersAdapter = HomeAdapter()
            rvFollowers.adapter = followersAdapter
            rvFollowers.setHasFixedSize(true)
        }
    }

    private fun setupOnRefresh(username: String) {
        binding.apply {
            btnErrorRetry.setOnClickListener {
                followersViewModel.onRefresh(username)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}