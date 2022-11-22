package com.projectgithub.presentation.following

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.projectgithub.R
import com.projectgithub.common.Resources
import com.projectgithub.data.repository.RemoteRepository
import com.projectgithub.data.source.remote.network.ApiConfig
import com.projectgithub.databinding.FragmentFollowingBinding
import com.projectgithub.presentation.factory.RemoteVMFactory
import com.projectgithub.presentation.home.adapter.HomeAdapter

class FollowingFragment : Fragment(R.layout.fragment_following) {

    private var _binding: FragmentFollowingBinding? = null
    private val binding get() = _binding!!

    private lateinit var followingAdapter: HomeAdapter
    private lateinit var followingViewModel: FollowingViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentFollowingBinding.bind(view)

        initObserver()
        initRecycler()
    }

    private fun initObserver() {
        val username = arguments?.getString("username").toString()
        val remoteRepository = RemoteRepository(ApiConfig.apiServices)
        val factory = RemoteVMFactory(remoteRepository)
        setupOnRefresh(username)
        followingViewModel = ViewModelProvider(this, factory)[FollowingViewModel::class.java]
        followingViewModel.getFollowing(username)
        followingViewModel.state.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resources.Loading -> {
                    binding.pbFollowing.visibility = View.VISIBLE
                    binding.rvFollowing.visibility = View.INVISIBLE
                    binding.tvErrorRetry.visibility = View.INVISIBLE
                    binding.btnErrorRetry.visibility = View.INVISIBLE
                }
                is Resources.Success -> {
                    binding.pbFollowing.visibility = View.INVISIBLE
                    binding.rvFollowing.visibility = View.VISIBLE
                    binding.tvErrorRetry.visibility = View.INVISIBLE
                    binding.btnErrorRetry.visibility = View.INVISIBLE
                    response.data?.let { followingAdapter.setData(it) }
                }
                is Resources.Error -> {
                    binding.pbFollowing.visibility = View.INVISIBLE
                    binding.rvFollowing.visibility = View.INVISIBLE
                    binding.tvErrorRetry.visibility = View.VISIBLE
                    binding.btnErrorRetry.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun initRecycler() {
        binding.apply {
            followingAdapter = HomeAdapter()
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