package com.projectgithub.presentation.following

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.projectgithub.R
import com.projectgithub.common.Resources
import com.projectgithub.data.Repository
import com.projectgithub.data.network.ApiConfig
import com.projectgithub.databinding.FragmentFollowingBinding
import com.projectgithub.presentation.home.adapter.HomeAdapter

class FollowingFragment : Fragment(R.layout.fragment_following) {

    private var _binding: FragmentFollowingBinding? = null
    private val binding get() = _binding!!

    private lateinit var followingViewModel: FollowingViewModel
    private lateinit var followingAdapter: HomeAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentFollowingBinding.bind(view)

        initObserver()
        initRecycler()
    }

    private fun initRecycler() {
        val repository = Repository(ApiConfig.apiServices)
        val username = arguments?.getString("username").toString()

        followingViewModel = FollowingViewModel(repository)
        followingViewModel.getFollowing(username)
        followingViewModel.state.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resources.Loading -> {
                    binding.pbFollowing.visibility = View.VISIBLE
                    binding.rvFollowing.visibility = View.INVISIBLE
                }
                is Resources.Success -> {
                    binding.pbFollowing.visibility = View.INVISIBLE
                    binding.rvFollowing.visibility = View.VISIBLE
                    response.data?.let { followingAdapter.setData(it) }
                }
                is Resources.Error -> {

                }
            }
        }
    }

    private fun initObserver() {
        binding.apply {
            followingAdapter = HomeAdapter()
            rvFollowing.adapter = followingAdapter
            rvFollowing.setHasFixedSize(true)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}