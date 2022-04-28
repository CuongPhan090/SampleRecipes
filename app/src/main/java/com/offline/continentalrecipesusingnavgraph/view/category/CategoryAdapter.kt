package com.offline.continentalrecipesusingnavgraph.view.category

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

class CategoryAdapter(val onItemClickListener: (Category) -> Any) :
    ListAdapter<Category, CategoryAdapter.CategoryViewHolder>(CategoryDiff()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(
            ListViewHolderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        ) {
            onItemClickListener(getItem(it))
        }
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class CategoryViewHolder(
        private val binding: ListViewHolderBinding,
        private val onItemClickedPosition: (Int) -> Any
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            showShimmer(true)
            itemView.setOnClickListener {
                onItemClickedPosition(adapterPosition)
            }
        }

        fun bind(category: Category) {
            binding.viewHolderTitle.text = category.name

            Glide.with(binding.root)
                .load(category.thumb)
                .error(R.drawable.ic_broken_image)
                .listener(object : RequestListener<Drawable> {
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

class CategoryDiff : DiffUtil.ItemCallback<Category>() {
    override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean =
        areItemsTheSame(oldItem, newItem)
}