package com.example.asteroidradar.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.asteroidradar.network_database.database.AsteroidData
import com.example.asteroidradar.databinding.AsteroidElementBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AsteroidRecyclerAdapter(private val clickListener: AsteroidRecyclerListener) : ListAdapter<DataItem, RecyclerView.ViewHolder>(AsteroidRecyclerDiffCallback()) {
    private val adapterScope = CoroutineScope(Dispatchers.Default)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolder -> {
                val asteroidDataItem = getItem(position) as DataItem.AsteroidRecyclerItem
                holder.bind(asteroidDataItem.asteroidData, clickListener)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder.from(parent)
    }

    fun addAndSubmitList(list: List<AsteroidData>?) {
        if(list != null) {
            adapterScope.launch {
                withContext(Dispatchers.Main) {
                    submitList(list.map { DataItem.AsteroidRecyclerItem(it) })
                }
            }
        }
    }

    class ViewHolder private constructor(private val binding: AsteroidElementBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: AsteroidData, clickListener: AsteroidRecyclerListener) {
            binding.asteroidData = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = AsteroidElementBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class AsteroidRecyclerDiffCallback : DiffUtil.ItemCallback<DataItem>() {
    override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem == newItem
    }

}


class AsteroidRecyclerListener(val clickListener: (id: Long) -> Unit) {
    fun onClick(asteroidData: AsteroidData) = clickListener(asteroidData.id)
}


sealed class DataItem {
    data class AsteroidRecyclerItem(val asteroidData: AsteroidData): DataItem() {
        override val id = asteroidData.id
    }

    abstract val id: Long
}
