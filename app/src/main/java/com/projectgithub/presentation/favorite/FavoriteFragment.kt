package com.projectgithub.presentation.favorite

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import com.google.android.material.snackbar.Snackbar
import com.projectgithub.R
import com.projectgithub.common.entityToResult
import com.projectgithub.common.setVisibilityGone
import com.projectgithub.common.setVisibilityVisible
import com.projectgithub.databinding.FragmentFavoriteBinding
import com.projectgithub.presentation.factory.ViewModelFactory
import com.projectgithub.presentation.favorite.adapter.FavoriteAdapter

class FavoriteFragment : Fragment(R.layout.fragment_favorite) {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    private val favoriteViewModel by viewModels<FavoriteViewModel> {
        ViewModelFactory.getInstance(
            requireContext()
        )
    }
    private lateinit var favoriteAdapter: FavoriteAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentFavoriteBinding.bind(view)

        setupToolbar()
        initObserver()
        initRecycler()
    }

    private fun initRecycler() {
        binding.apply {
            favoriteAdapter = FavoriteAdapter()
            rvFavorite.adapter = favoriteAdapter
            rvFavorite.setHasFixedSize(true)
        }
    }

    private fun initObserver() {
        favoriteViewModel.getUser.observe(viewLifecycleOwner) { userEntity ->
            if (userEntity.isNotEmpty()) {
                checkFavoriteUser(true)
                favoriteAdapter.setData(userEntity.map { it.entityToResult() })
            } else {
                checkFavoriteUser(false)
            }
        }
    }

    private fun checkFavoriteUser(isDataFound: Boolean) {
        if (isDataFound) {
            binding.rvFavorite.setVisibilityVisible()
            binding.lottieFavorite.setVisibilityGone()
            binding.tvFavorite.setVisibilityGone()
        } else {
            binding.rvFavorite.setVisibilityGone()
            binding.lottieFavorite.setVisibilityVisible()
            binding.tvFavorite.setVisibilityVisible()
        }
    }

    private fun setupToolbar() {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_favorite, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.delete_all_menu -> {
                        favoriteViewModel.deleteAllUser()
                        showSnackBar()
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun showSnackBar() {
        Snackbar.make(
            binding.root,
            getString(R.string.all_user_removed),
            Snackbar.LENGTH_SHORT
        ).setAction(getString(R.string.okay)) {}
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}