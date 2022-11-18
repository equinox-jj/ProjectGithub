package com.projectgithub.presentation.home

import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.projectgithub.data.model.UserData
import com.projectgithub.databinding.ItemUserListBinding

class HomeVH(private val binding: ItemUserListBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(data: UserData) {
        binding.apply {
            ivAvatar.load(data.avatar) {
                crossfade(800)
                transformations(RoundedCornersTransformation(25f))
            }
            tvName.text = data.name
            tvUsername.text = data.username
            tvLocation.text = data.location

            cardViewUser.setOnClickListener {
                val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment(data)
                it.findNavController().navigate(action)
            }
        }
    }
}
