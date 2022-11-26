package com.projectgithub.presentation.followers

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.projectgithub.R
import com.projectgithub.common.Constants.NAME_ARGS
import com.projectgithub.common.Resources
import com.projectgithub.common.setVisibilityGone
import com.projectgithub.common.setVisibilityVisible
import com.projectgithub.databinding.FragmentFollowersBinding
import com.projectgithub.presentation.factory.ViewModelFactory
import com.projectgithub.presentation.followers.adapter.FollowAdapter

class FollowersFragment : Fragment(R.layout.fragment_followers) {

    private var _binding: FragmentFollowersBinding? = null
    private val binding get() = _binding!!

    private lateinit var followersAdapter: FollowAdapter
    private val followersViewModel by viewModels<FollowersViewModel> { ViewModelFactory.getInstance() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentFollowersBinding.bind(view)

        initObserver()
        initRecycler()
    }

    private fun initObserver() {
        val username = arguments?.getString(NAME_ARGS).toString()

        followersViewModel.getFollowers(username)
        followersViewModel.state.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resources.Loading -> {
                    isLoading(true)
                    isError(false)
                }
                is Resources.Success -> {
                    isLoading(false)
                    isError(false)
                    response.data?.let { followersAdapter.setData(it) }
                }
                is Resources.Error -> {
                    isLoading(false)
                    isError(true)
                    setupOnRefresh(username)
                }
            }
        }
    }

    private fun isLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.pbFollowers.setVisibilityVisible()
            binding.rvFollowers.setVisibilityGone()
        } else {
            binding.pbFollowers.setVisibilityGone()
            binding.rvFollowers.setVisibilityVisible()
        }
    }

    private fun isError(isError: Boolean) {
        if (isError) {
            binding.tvErrorRetry.setVisibilityVisible()
            binding.btnErrorRetry.setVisibilityVisible()
        } else {
            binding.tvErrorRetry.setVisibilityGone()
            binding.btnErrorRetry.setVisibilityGone()
        }
    }

    private fun initRecycler() {
        binding.apply {
            followersAdapter = FollowAdapter()
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