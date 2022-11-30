package com.projectgithub.presentation.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.projectgithub.data.model.UserData
import com.projectgithub.databinding.ItemUserListBinding

class HomeAdapter(private val data: ArrayList<UserData>) : RecyclerView.Adapter<HomeVH>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    interface OnItemClickCallback {
        fun onItemClick(data: UserData)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeVH {
        val binding = ItemUserListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeVH(binding)
    }

    override fun onBindViewHolder(holder: HomeVH, position: Int) {
        holder.bind(data[position])
        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClick(data[holder.adapterPosition])
        }
    }

    override fun getItemCount(): Int = data.size

}