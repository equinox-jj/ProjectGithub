package com.projectgithub.presentation.home.adapter

import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.projectgithub.data.model.ResultItem
import com.projectgithub.databinding.ItemUserListBinding
import com.projectgithub.presentation.home.HomeFragmentDirections

class HomeVH(private val binding: ItemUserListBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(data: ResultItem?) {
        binding.apply {
            ivAvatar.load(data?.avatarUrl) {
                crossfade(800)
                transformations(RoundedCornersTransformation(25f))
            }
            tvName.text = data?.login
            tvUsername.text = data?.htmlUrl

            cardViewUser.setOnClickListener {
                val action = data?.let { HomeFragmentDirections.actionHomeFragmentToDetailFragment(it.login) }
                if (action != null) { it.findNavController().navigate(action) }
            }
        }
    }
}
