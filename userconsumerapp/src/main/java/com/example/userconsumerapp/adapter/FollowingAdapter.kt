package com.example.userconsumerapp.adapter

import android.view.LayoutInflater

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.userconsumerapp.data.Following
import com.example.userconsumerapp.databinding.ItemRowGithubBinding

class FollowingAdapter(private val listFollowingGithub: ArrayList<Following>): RecyclerView.Adapter<FollowingAdapter.ListViewHolder>() {

    inner class ListViewHolder(private val binding: ItemRowGithubBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(following: Following) {
            with(binding){
                Glide.with(itemView.context)
                    .load(following.avatar)
                    .into(imgItemPhoto)
                tvItemName.text = following.username
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemRowGithubBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listFollowingGithub[position])
    }

    override fun getItemCount(): Int {
        return listFollowingGithub.size
    }

}