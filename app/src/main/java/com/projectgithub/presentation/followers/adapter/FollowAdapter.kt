package com.projectgithub.presentation.followers.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.projectgithub.common.DifferRecycler
import com.projectgithub.data.model.ResultItem
import com.projectgithub.databinding.ItemUserListBinding

class FollowAdapter: RecyclerView.Adapter<FollowVH>() {

    private var followItem = listOf<ResultItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowVH {
        val binding = ItemUserListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FollowVH(binding)
    }

    override fun onBindViewHolder(holder: FollowVH, position: Int) {
        holder.bind(followItem[position])
    }

    override fun getItemCount(): Int = followItem.size

    fun setData(newData: List<ResultItem>) {
        val callback = DifferRecycler(followItem, newData)
        val result = DiffUtil.calculateDiff(callback)
        followItem = newData
        result.dispatchUpdatesTo(this)
    }
}