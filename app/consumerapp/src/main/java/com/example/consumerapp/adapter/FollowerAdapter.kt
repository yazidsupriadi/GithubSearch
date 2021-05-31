package com.example.consumerapp.adapter

import android.view.LayoutInflater

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.consumerapp.data.Follower
import com.example.consumerapp.databinding.ItemRowGithubBinding

class FollowerAdapter(private val listFollowerGithub: ArrayList<Follower>): RecyclerView.Adapter<FollowerAdapter.ListViewHolder>() {

    inner class ListViewHolder(private val binding: ItemRowGithubBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(follower: Follower) {
            with(binding){
                Glide.with(itemView.context)
                    .load(follower.avatar)
                    .into(imgItemPhoto)
                tvItemName.text = follower.username
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemRowGithubBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listFollowerGithub[position])
    }

    override fun getItemCount(): Int {
        return listFollowerGithub.size
    }

}