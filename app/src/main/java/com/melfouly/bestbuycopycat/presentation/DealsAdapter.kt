package com.melfouly.bestbuycopycat.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.melfouly.bestbuycopycat.databinding.DealsGridItemBinding
import com.melfouly.bestbuycopycat.databinding.DealsListItemBinding
import com.melfouly.bestbuycopycat.domain.model.Category

private const val VIEW_TYPE_LIST = 0
private const val VIEW_TYPE_GRID = 1

class DealsAdapter(private val usedDesignLayout: Int) :
    ListAdapter<Category, RecyclerView.ViewHolder>(DealDiffCallback()) {

    override fun getItemViewType(position: Int): Int {
        return usedDesignLayout
    }

    class ListViewHolder(private val binding: DealsListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Category) {
            binding.title.text = item.strCategory
            binding.rate.text = "5.0"
            binding.price.text = "EGP100"
            Glide.with(binding.root).load(item.strCategoryThumb).into(binding.imageView)
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

        fun bind(item: Category) {
            binding.title.text = item.strCategory
            binding.rate.text = "5.0"
            binding.price.text = "EGP100"
            Glide.with(binding.root).load(item.strCategoryThumb).into(binding.imageView)
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
                val item = getItem(position) as Category
                holder.bind(item)
            }

            is GridViewHolder -> {
                val item = getItem(position) as Category
                holder.bind(item)
            }
        }
    }
}

class DealDiffCallback : DiffUtil.ItemCallback<Category>() {
    override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
        return oldItem.idCategory == newItem.idCategory
    }

    override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
        return oldItem == newItem
    }
}