package com.udacity.asteroidradar.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.databinding.LinearViewItemBinding

class PhotoLinearAdapter(val onClickListener: OnClickListener) : ListAdapter<Asteroid, PhotoLinearAdapter.AsteroidViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoLinearAdapter.AsteroidViewHolder {
        return AsteroidViewHolder(LinearViewItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: PhotoLinearAdapter.AsteroidViewHolder, position: Int) {
        val asteroid = getItem(position)
        holder.bind(asteroid)
        holder.itemView.setOnClickListener{
            onClickListener.onClick(asteroid)
        }
    }

    class AsteroidViewHolder(private var binding: LinearViewItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(asteroid: Asteroid) {
            binding.asteroid = asteroid
            binding.executePendingBindings()
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Asteroid>() {
        override fun areItemsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem.id == newItem.id
        }
    }

    class OnClickListener(val clickListener: (asteroid: Asteroid) -> Unit) {
        fun onClick(asteroid: Asteroid) = clickListener(asteroid)
    }
}

