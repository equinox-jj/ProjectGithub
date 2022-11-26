package com.projectgithub.presentation.detail

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
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
import com.projectgithub.data.source.local.entity.UserEntity
import com.projectgithub.databinding.FragmentDetailBinding
import com.projectgithub.presentation.detail.adapter.ViewPagerAdapter
import com.projectgithub.presentation.followers.FollowersFragment
import com.projectgithub.presentation.following.FollowingFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : Fragment(R.layout.fragment_detail) {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<DetailFragmentArgs>()
    private val detailViewModel by viewModels<DetailViewModel>()

    private lateinit var userEntity: UserEntity
    private lateinit var savedMenuItem: MenuItem
    private var isUserSaved = false
    private var savedUsername = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentDetailBinding.bind(view)

        initObserver()
        setupViewPager()
        setupToolbar()
    }

    private fun setupToolbar() {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_detail, menu)
                savedMenuItem = menu.findItem(R.id.fav_menu)
                checkIsUserSaved(savedMenuItem)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.fav_menu -> {
                        if (!isUserSaved) {
                            insertUser(menuItem)
                        } else {
                            deleteUser(menuItem)
                        }
                        true
                    }
                    R.id.share_menu -> {
                        val sendIntent: Intent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, userEntity.url)
                            type = "text/plain"
                        }
                        val shareIntent = Intent.createChooser(sendIntent, null)
                        startActivity(shareIntent)
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
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

            data?.let {
                userEntity = UserEntity(
                    id = it.id,
                    avatar = it.avatarUrl,
                    username = it.login,
                    url = it.htmlUrl
                )
            }
        }
    }

    private fun isLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.constraintDet.setVisibilityGone()
        } else {
            binding.constraintDet.setVisibilityVisible()
        }
    }

    private fun checkIsUserSaved(savedMenuItem: MenuItem) {
        detailViewModel.getUser.observe(viewLifecycleOwner) { entity ->
            try {
                entity.forEach { result ->
                    if (result.username == args.username) {
                        changeFavMenuColor(savedMenuItem, R.color.icon_favorite_color)
                        savedUsername = result.username
                        isUserSaved = true
                    }
                }
            } catch (e: Exception) {
                isUserSaved = false
            }
        }
    }

    private fun insertUser(menuItem: MenuItem) {
        detailViewModel.insertUser(userEntity)
        showSnackBarFavorite("${userEntity.username} saved.")
        changeFavMenuColor(menuItem, R.color.icon_favorite_color)
        isUserSaved = true
    }

    private fun deleteUser(menuItem: MenuItem) {
        detailViewModel.deleteUser(userEntity)
        showSnackBarFavorite("${userEntity.username} removed.")
        changeFavMenuColor(menuItem, R.color.icon_toolbar_color)
        isUserSaved = false
    }

    private fun changeFavMenuColor(item: MenuItem, color: Int) {
        item.icon?.setTint(ContextCompat.getColor(requireContext(), color))
    }

    private fun showSnackBarFavorite(message: String) {
        Snackbar.make(binding.constraintDet, message, Snackbar.LENGTH_SHORT).setAction(getString(R.string.okay)) {}
            .show()
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
        changeFavMenuColor(savedMenuItem, R.color.icon_toolbar_color)
        _binding = null
    }
}