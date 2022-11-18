package com.projectgithub.presentation.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.projectgithub.common.DifferRecycler
import com.projectgithub.data.model.ResultItem
import com.projectgithub.databinding.ItemUserListBinding

class HomeAdapter : RecyclerView.Adapter<HomeVH>() {

    private var searchUser = listOf<ResultItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeVH {
        val binding = ItemUserListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeVH(binding)
    }

    override fun onBindViewHolder(holder: HomeVH, position: Int) {
        holder.bind(searchUser[position])
    }

    override fun getItemCount(): Int = searchUser.size

    fun setData(newData: List<ResultItem>) {
        val callback = DifferRecycler(searchUser, newData)
        val result = DiffUtil.calculateDiff(callback)
        searchUser = newData
        result.dispatchUpdatesTo(this)
    }
}