package com.projectgithub.presentation.favorite.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.projectgithub.common.DifferRecycler
import com.projectgithub.data.model.ResultItem
import com.projectgithub.databinding.ItemFavoriteListBinding

class FavoriteAdapter: RecyclerView.Adapter<FavoriteVH>() {

    private var favoriteItem = listOf<ResultItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteVH {
        val binding = ItemFavoriteListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteVH(binding)
    }

    override fun onBindViewHolder(holder: FavoriteVH, position: Int) {
        holder.bind(favoriteItem[position])
    }

    override fun getItemCount(): Int = favoriteItem.size

    fun setData(newData: List<ResultItem>) {
        val callback = DifferRecycler(favoriteItem, newData)
        val result = DiffUtil.calculateDiff(callback)
        favoriteItem = newData
        result.dispatchUpdatesTo(this)
    }

}