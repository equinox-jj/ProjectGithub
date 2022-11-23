package com.projectgithub.presentation.favorite.adapter

import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.projectgithub.data.model.ResultItem
import com.projectgithub.databinding.ItemFavoriteListBinding
import com.projectgithub.presentation.favorite.FavoriteFragmentDirections

class FavoriteVH(private val binding: ItemFavoriteListBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(data: ResultItem?) {
        binding.apply {
            ivAvatarFav.load(data?.avatarUrl) {
                crossfade(800)
                transformations(RoundedCornersTransformation(25f))
            }
            tvNameFav.text = data?.login
            tvUsernameFav.text = data?.htmlUrl

            cardViewUserFav.setOnClickListener { view ->
                val action = data?.let { FavoriteFragmentDirections.actionFavoriteFragmentToDetailFragment(it.login) }
                if (action != null) {
                    view.findNavController().navigate(action)
                }
            }
        }
    }

}