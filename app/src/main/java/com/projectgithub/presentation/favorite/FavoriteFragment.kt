package com.projectgithub.presentation.favorite

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.projectgithub.R
import com.projectgithub.common.entityToResult
import com.projectgithub.data.repository.LocalRepository
import com.projectgithub.data.source.local.database.UserDatabase
import com.projectgithub.databinding.FragmentFavoriteBinding
import com.projectgithub.presentation.factory.LocalVMFactory
import com.projectgithub.presentation.favorite.adapter.FavoriteAdapter

class FavoriteFragment : Fragment(R.layout.fragment_favorite) {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    private lateinit var favoriteViewModel: FavoriteViewModel
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
        val userDb = UserDatabase.getInstance(requireContext())
        val localRepository = LocalRepository(userDb)
        val factory = LocalVMFactory(localRepository)

        favoriteViewModel = ViewModelProvider(this, factory)[FavoriteViewModel::class.java]
        favoriteViewModel.getUser.observe(viewLifecycleOwner) { userEntity ->
            if (userEntity.isNotEmpty()) {
                binding.rvFavorite.visibility = View.VISIBLE
                binding.lottieFavorite.visibility = View.INVISIBLE
                binding.tvFavorite.visibility = View.INVISIBLE
                favoriteAdapter.setData(userEntity.map { it.entityToResult() })

            } else {
                binding.rvFavorite.visibility = View.INVISIBLE
                binding.lottieFavorite.visibility = View.VISIBLE
                binding.tvFavorite.visibility = View.VISIBLE
            }
        }
    }

    private fun setupToolbar() {
        binding.toolbarFavorite.apply {
            val menuHost: MenuHost = this@apply
            menuHost.addMenuProvider(object : MenuProvider {
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    menuInflater.inflate(R.menu.menu_favorite, menu)
                }

                override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                    when (menuItem.itemId) {
                        R.id.delete_all_menu -> {
                            favoriteViewModel.deleteAllUser()
                            showSnackBar()
                        }
                    }
                    return true
                }
            })
        }
    }

    private fun showSnackBar() {
        Snackbar.make(
            binding.root,
            "All User Removed.",
            Snackbar.LENGTH_SHORT
        ).setAction("Okay.") {}
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}