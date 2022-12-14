package com.projectgithub.presentation.followers.adapter

import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.projectgithub.data.model.ResultItem
import com.projectgithub.databinding.ItemUserListBinding

class FollowVH(private val binding: ItemUserListBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(data: ResultItem?) {
        binding.apply {
            ivAvatar.load(data?.avatarUrl) {
                crossfade(800)
                transformations(RoundedCornersTransformation(25f))
            }
            tvName.text = data?.login
            tvUsername.text = data?.htmlUrl
        }
    }
}