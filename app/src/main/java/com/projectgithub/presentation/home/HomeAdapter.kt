package com.projectgithub.presentation.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.projectgithub.data.model.UserData
import com.projectgithub.databinding.ItemUserListBinding

class HomeAdapter(private val data: ArrayList<UserData>) : RecyclerView.Adapter<HomeVH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeVH {
        val binding = ItemUserListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeVH(binding)
    }

    override fun onBindViewHolder(holder: HomeVH, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size

}