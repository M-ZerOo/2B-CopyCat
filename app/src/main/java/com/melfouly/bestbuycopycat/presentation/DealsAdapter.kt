package com.melfouly.bestbuycopycat.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.melfouly.bestbuycopycat.databinding.DealsGridItemBinding
import com.melfouly.bestbuycopycat.databinding.DealsListItemBinding
import com.melfouly.bestbuycopycat.domain.model.Product

private const val VIEW_TYPE_LIST = 0
private const val VIEW_TYPE_GRID = 1

class DealsAdapter(private val usedDesignLayout: Int) :
    ListAdapter<Product, RecyclerView.ViewHolder>(DealDiffCallback()) {

    override fun getItemViewType(position: Int): Int {
        return usedDesignLayout
    }

    class ListViewHolder(private val binding: DealsListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Product) {
            binding.title.text = item.name
            binding.rate.text = item.rate
            binding.price.text = item.price
            Glide.with(binding.root).load(item.image).into(binding.imageView)
        }

        companion object {
            fun from(parent: ViewGroup): ListViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = DealsListItemBinding.inflate(layoutInflater, parent, false)

                return ListViewHolder(binding)
            }
        }
    }

    class GridViewHolder(private val binding: DealsGridItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Product) {
            binding.title.text = item.name
            binding.rate.text = item.rate
            binding.price.text = item.price
            Glide.with(binding.root).load(item.image).into(binding.imageView)
        }

        companion object {
            fun from(parent: ViewGroup): GridViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = DealsGridItemBinding.inflate(layoutInflater, parent, false)

                return GridViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_LIST -> ListViewHolder.from(parent)
            VIEW_TYPE_GRID -> GridViewHolder.from(parent)
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ListViewHolder -> {
                val item = getItem(position) as Product
                holder.bind(item)
            }

            is GridViewHolder -> {
                val item = getItem(position) as Product
                holder.bind(item)
            }
        }
    }
}

class DealDiffCallback : DiffUtil.ItemCallback<Product>() {
    override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem == newItem
    }
}