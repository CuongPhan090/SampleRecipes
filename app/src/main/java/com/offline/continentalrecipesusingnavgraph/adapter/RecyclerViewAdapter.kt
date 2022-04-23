package com.offline.continentalrecipesusingnavgraph.adapter

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.offline.continentalrecipesusingnavgraph.R
import com.offline.continentalrecipesusingnavgraph.databinding.ListViewHolderBinding
import com.offline.continentalrecipesusingnavgraph.model.Category

class RecyclerViewAdapter: ListAdapter<Category, RecyclerViewAdapter.RecyclerViewHolder>(RecyclerViewDiff()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        return RecyclerViewHolder(ListViewHolderBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class RecyclerViewHolder(private val binding: ListViewHolderBinding): RecyclerView.ViewHolder(binding.root) {

        init {
            showShimmer(true)
        }

        fun bind(category: Category) {
            binding.viewHolderTitle.text = category.categoryName

            Glide.with(binding.root)
                .load(category.categoryThumb)
                .error(R.drawable.ic_broken_image)
                .listener(object: RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        showShimmer(false)
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        showShimmer(false)
                        binding.viewHolderTitle.visibility = View.VISIBLE
                        return false
                    }

                })
                .into(binding.viewHolderBackground)
        }

        private fun showShimmer(show: Boolean) {
            if (show) {
                binding.shimmerLayout.startShimmer()
                binding.shimmerLayout.visibility = View.VISIBLE
            } else {
                binding.shimmerLayout.stopShimmer()
                binding.shimmerLayout.visibility = View.GONE
            }
        }
    }
}

class RecyclerViewDiff: DiffUtil.ItemCallback<Category>() {
    override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean = areItemsTheSame(oldItem, newItem)
}