package com.projectgithub.presentation.detail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import coil.load
import coil.transform.CircleCropTransformation
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import com.projectgithub.R
import com.projectgithub.common.Constants.NAME_ARGS
import com.projectgithub.common.Resources
import com.projectgithub.common.setVisibilityGone
import com.projectgithub.common.setVisibilityVisible
import com.projectgithub.data.model.DetailResponse
import com.projectgithub.databinding.FragmentDetailBinding
import com.projectgithub.presentation.detail.adapter.ViewPagerAdapter
import com.projectgithub.presentation.factory.ViewModelFactory
import com.projectgithub.presentation.followers.FollowersFragment
import com.projectgithub.presentation.following.FollowingFragment

class DetailFragment : Fragment(R.layout.fragment_detail) {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<DetailFragmentArgs>()
    private val detailViewModel by viewModels<DetailViewModel> { ViewModelFactory.getInstance() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentDetailBinding.bind(view)

        initObserver()
        setupViewPager()
    }

    private fun setupViewPager() {
        val tabLayout = binding.tlDet
        val viewPager = binding.vpDet

        val bundle = Bundle()
        bundle.putString(NAME_ARGS, args.username)

        val fragmentTabs = ArrayList<Fragment>()
        fragmentTabs.add(FollowersFragment())
        fragmentTabs.add(FollowingFragment())

        val titles = ArrayList<String>()
        titles.add(getString(R.string.followers))
        titles.add(getString(R.string.following))

        viewPager.adapter = ViewPagerAdapter(bundle, fragmentTabs, fragment = this@DetailFragment)

        TabLayoutMediator(tabLayout, viewPager) { tabs, position ->
            tabs.text = titles[position]
        }.attach()
    }

    private fun initObserver() {
        val username = args.username
        detailViewModel.getUserByName(username)
        detailViewModel.state.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resources.Loading -> {
                    isLoading(true)
                }
                is Resources.Success -> {
                    isLoading(false)
                    response.data?.let { initView(it) }
                }
                is Resources.Error -> {
                    isLoading(false)
                    showErrorSnackBar(username)
                }
            }
        }
    }

    private fun initView(data: DetailResponse?) {
        binding.apply {
            ivAvatarDet.load(data?.avatarUrl) {
                crossfade(800)
            }
            ivAvatarSmallDet.load(data?.avatarUrl) {
                crossfade(800)
                transformations(CircleCropTransformation())
            }
            if (data?.company != null) tvCompanyDet.text = data.company.toString()
            else tvCompanyDet.text = getString(R.string.no_data)
            if (data?.location != null) tvLocationDet.text = data.location
            else tvLocationDet.text = getString(R.string.no_data)
            tvUsernameDet.text = data?.name
            tvRepository.text = data?.publicRepos.toString()
            tvFollowers.text = data?.followers.toString()
            tvFollowing.text = data?.following.toString()
        }
    }

    private fun isLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.ivAvatarDet.setVisibilityGone()
            binding.constraintAblDet.setVisibilityGone()
        } else {
            binding.ivAvatarDet.setVisibilityVisible()
            binding.constraintAblDet.setVisibilityVisible()
        }
    }

    private fun showErrorSnackBar(username: String) {
        Snackbar.make(binding.clDet, getString(R.string.error_when_load_the_data), Snackbar.LENGTH_INDEFINITE)
            .setAction(getString(R.string.retry)) {
                detailViewModel.onRefresh(username)
            }
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}