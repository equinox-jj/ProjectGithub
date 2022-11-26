package com.projectgithub.presentation.following

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.projectgithub.R
import com.projectgithub.common.Constants.NAME_ARGS
import com.projectgithub.common.Resources
import com.projectgithub.common.setVisibilityGone
import com.projectgithub.common.setVisibilityVisible
import com.projectgithub.databinding.FragmentFollowingBinding
import com.projectgithub.presentation.factory.ViewModelFactory
import com.projectgithub.presentation.followers.adapter.FollowAdapter

class FollowingFragment : Fragment(R.layout.fragment_following) {

    private var _binding: FragmentFollowingBinding? = null
    private val binding get() = _binding!!

    private lateinit var followingAdapter: FollowAdapter
    private val followingViewModel by viewModels<FollowingViewModel> { ViewModelFactory.getInstance() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentFollowingBinding.bind(view)

        initObserver()
        initRecycler()
    }

    private fun initObserver() {
        val username = arguments?.getString(NAME_ARGS).toString()

        followingViewModel.getFollowing(username)
        followingViewModel.state.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resources.Loading -> {
                    isLoading(true)
                    isError(false)
                }
                is Resources.Success -> {
                    isLoading(false)
                    isError(false)
                    response.data?.let { followingAdapter.setData(it) }
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
            binding.pbFollowing.setVisibilityVisible()
            binding.rvFollowing.setVisibilityGone()
        } else {
            binding.pbFollowing.setVisibilityGone()
            binding.rvFollowing.setVisibilityVisible()
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
            followingAdapter = FollowAdapter()
            rvFollowing.adapter = followingAdapter
            rvFollowing.setHasFixedSize(true)
        }
    }

    private fun setupOnRefresh(username: String) {
        binding.apply {
            btnErrorRetry.setOnClickListener {
                followingViewModel.onRefresh(username)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}