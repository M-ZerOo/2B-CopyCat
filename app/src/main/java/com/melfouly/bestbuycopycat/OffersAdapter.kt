package com.melfouly.bestbuycopycat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.melfouly.bestbuycopycat.databinding.ChildListItemBinding
import com.melfouly.bestbuycopycat.databinding.HeaderListItemBinding

// Create constant values for header and child.
private const val VIEW_TYPE_HEADER = 0
private const val VIEW_TYPE_CHILD = 1

class OffersAdapter :
    ListAdapter<DataItem, RecyclerView.ViewHolder>(OfferDiffCallback()) {

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is DataItem.HeaderItem -> VIEW_TYPE_HEADER
            is DataItem.ChildItem -> VIEW_TYPE_CHILD
        }
    }

    class HeaderViewHolder(private val binding: HeaderListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: DataItem.HeaderItem) {
            binding.cardTitle.text = item.title
            binding.cardPrice.text = item.price
        }

        companion object {
            fun from(parent: ViewGroup): HeaderViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = HeaderListItemBinding.inflate(layoutInflater, parent, false)

                return HeaderViewHolder(binding)
            }
        }
    }

    class ChildViewHolder(private val binding: ChildListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: DataItem.ChildItem) {
            binding.cardTitle.text = item.title
            binding.cardPrice.text = item.price
        }

        companion object {
            fun from(parent: ViewGroup): ChildViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ChildListItemBinding.inflate(layoutInflater, parent, false)

                return ChildViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_HEADER -> HeaderViewHolder.from(parent)
            VIEW_TYPE_CHILD -> ChildViewHolder.from(parent)
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is HeaderViewHolder -> {
                val headerItem = getItem(position) as DataItem.HeaderItem
                holder.bind(headerItem)
            }

            is ChildViewHolder -> {
                val childItem = getItem(position) as DataItem.ChildItem
                holder.bind(childItem)
            }
        }
    }
}

class OfferDiffCallback : DiffUtil.ItemCallback<DataItem>() {
    override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem == newItem
    }
}

sealed class DataItem {
    abstract val id: Int // Used this line so we can use DiffUtil and Check if areItemsTheSame.

    data class ChildItem(
        override val id: Int = 2,
        val title: String,
        val price: String
    ) : DataItem()

    data class HeaderItem(
        override val id: Int = 0,
        val title: String,
        val price: String
    ) : DataItem()
}