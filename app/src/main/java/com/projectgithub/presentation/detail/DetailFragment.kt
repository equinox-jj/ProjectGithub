package com.projectgithub.presentation.detail

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.setupWithNavController
import coil.load
import coil.transform.CircleCropTransformation
import com.google.android.material.tabs.TabLayoutMediator
import com.projectgithub.R
import com.projectgithub.common.Resources
import com.projectgithub.data.Repository
import com.projectgithub.data.model.DetailResponse
import com.projectgithub.data.network.ApiConfig
import com.projectgithub.databinding.FragmentDetailBinding
import com.projectgithub.presentation.ViewModelProviderFactory
import com.projectgithub.presentation.detail.adapter.ViewPagerAdapter
import com.projectgithub.presentation.followers.FollowersFragment
import com.projectgithub.presentation.following.FollowingFragment

class DetailFragment : Fragment(R.layout.fragment_detail) {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<DetailFragmentArgs>()

    private lateinit var detailViewModel: DetailViewModel
    private lateinit var repository: Repository
    private lateinit var factory: ViewModelProviderFactory

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentDetailBinding.bind(view)

        initObserver()
        setupViewPager()
        setupToolbar()
    }

    private fun setupToolbar() {
        binding.toolbarDet.apply {
            setNavigationIcon(R.drawable.ic_baseline_arrow_back_ios_new_24)
            setupWithNavController(findNavController())

            val menuHost: MenuHost = this@apply
            menuHost.addMenuProvider(object : MenuProvider {
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    menuInflater.inflate(R.menu.menu_detail, menu)
                }

                override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                    return true
                }
            }, viewLifecycleOwner, Lifecycle.State.RESUMED)
        }
    }

    private fun setupViewPager() {
        val tabLayout = binding.tlDet
        val viewPager = binding.vpDet

        val bundle = Bundle()
        bundle.putString("username", args.username)

        val fragmentTabs = ArrayList<Fragment>()
        fragmentTabs.add(FollowersFragment())
        fragmentTabs.add(FollowingFragment())

        val titles = ArrayList<String>()
        titles.add("Followers")
        titles.add("Following")

        viewPager.adapter = ViewPagerAdapter(bundle, fragmentTabs, fragment = this@DetailFragment)

        TabLayoutMediator(tabLayout, viewPager) { tabs, position ->
            tabs.text = titles[position]
        }.attach()
    }

    private fun initObserver() {
        val username = args.username

        repository = Repository(ApiConfig.apiServices)
        factory = ViewModelProviderFactory(repository)
        detailViewModel = ViewModelProvider(this, factory)[DetailViewModel::class.java]

        detailViewModel.getUserByName(username)
        detailViewModel.state.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resources.Loading -> {
                    binding.pbDet.visibility = View.VISIBLE
                    binding.constraintDet.visibility = View.GONE
                    binding.ablDet.visibility = View.GONE
                }
                is Resources.Success -> {
                    binding.pbDet.visibility = View.GONE
                    binding.constraintDet.visibility = View.VISIBLE
                    binding.ablDet.visibility = View.VISIBLE
                    response.data?.let { initView(it) }
                }
                is Resources.Error -> {
                    Toast.makeText(
                        context,
                        "Check your internet connection",
                        Toast.LENGTH_SHORT
                    ).show()
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
            ctlDet.title = data?.name
            tvUsernameDet.text = data?.login
            tvCompanyDet.text = data?.company.toString()
            tvLocationDet.text = data?.location
            tvRepository.text = data?.publicRepos.toString()
            tvFollowers.text = data?.followers.toString()
            tvFollowers.text = data?.following.toString()
            setupOnRefresh(data!!.login)
        }
    }

    private fun setupOnRefresh(username: String) {
        binding.apply {
            refreshDetail.setOnRefreshListener {
                detailViewModel.onRefresh(username)
                refreshDetail.isRefreshing = false
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}