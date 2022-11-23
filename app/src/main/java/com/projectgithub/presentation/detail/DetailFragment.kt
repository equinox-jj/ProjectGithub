package com.projectgithub.presentation.detail

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import coil.transform.CircleCropTransformation
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import com.projectgithub.R
import com.projectgithub.common.Resources
import com.projectgithub.data.model.DetailResponse
import com.projectgithub.data.repository.LocalRepository
import com.projectgithub.data.repository.RemoteRepository
import com.projectgithub.data.source.local.database.UserDatabase
import com.projectgithub.data.source.local.entity.UserEntity
import com.projectgithub.data.source.remote.network.ApiConfig
import com.projectgithub.databinding.FragmentDetailBinding
import com.projectgithub.presentation.detail.adapter.ViewPagerAdapter
import com.projectgithub.presentation.factory.ViewModelFactory
import com.projectgithub.presentation.followers.FollowersFragment
import com.projectgithub.presentation.following.FollowingFragment

class DetailFragment : Fragment(R.layout.fragment_detail) {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<DetailFragmentArgs>()
    private lateinit var detailViewModel: DetailViewModel

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
        binding.toolbarDet.apply {
            setNavigationIcon(R.drawable.ic_baseline_arrow_back_ios_new_24)


            val menuHost: MenuHost = this@apply
            menuHost.addMenuProvider(object : MenuProvider {
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    menuInflater.inflate(R.menu.menu_detail, menu)
                    savedMenuItem = menu.findItem(R.id.fav_menu)
                    checkIsUserSaved(savedMenuItem)
                }

                override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                    if (menuItem.itemId == R.id.fav_menu && !isUserSaved) {
                        insertUser(menuItem)
                    } else if (menuItem.itemId == R.id.fav_menu && isUserSaved) {
                        deleteUser(menuItem)
                    }
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
        val userDb = UserDatabase.getInstance(requireContext())
        val remoteRepository = RemoteRepository(ApiConfig.apiServices)
        val localRepository = LocalRepository(userDb)
        val factory = ViewModelFactory(remoteRepository, localRepository)

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
            ctlDet.title = data?.name
            tvUsernameDet.text = data?.login
            tvCompanyDet.text = data?.company.toString()
            tvLocationDet.text = data?.location
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

    private fun checkIsUserSaved(savedMenuItem: MenuItem) {
        detailViewModel.getUser.observe(viewLifecycleOwner) { entity ->
            try {
                entity.forEach { result ->
                    if (result.username == args.username) {
                        changeFavMenuColor(savedMenuItem, R.color.purple_700)
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
        changeFavMenuColor(menuItem, R.color.purple_700)
        isUserSaved = true
    }

    private fun deleteUser(menuItem: MenuItem) {
        detailViewModel.deleteUser(userEntity)
        showSnackBarFavorite("${userEntity.username} removed.")
        changeFavMenuColor(menuItem, R.color.grey)
        isUserSaved = false
    }

    private fun changeFavMenuColor(item: MenuItem, color: Int) {
        item.icon?.setTint(ContextCompat.getColor(requireContext(), color))
    }

    private fun showSnackBarFavorite(message: String) {
        Snackbar.make(
            binding.constraintDet,
            message,
            Snackbar.LENGTH_SHORT
        ).setAction("Okay") {}
            .show()
    }

    private fun showErrorSnackBar(username: String) {
        Snackbar.make(
            requireView(),
            "Error when loading data.",
            Snackbar.LENGTH_INDEFINITE
        ).setAction("Retry") {
            detailViewModel.onRefresh(username)
        }.setAnchorView(binding.clDet).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}