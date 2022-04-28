package com.offline.continentalrecipesusingnavgraph.view.favorite

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
import com.offline.continentalrecipesusingnavgraph.data.local.MealEntity
import com.offline.continentalrecipesusingnavgraph.databinding.ListViewHolderBinding

class FavoriteAdapter(val onItemClickListener: (View, MealEntity, String) -> Any) :
    ListAdapter<MealEntity, FavoriteAdapter.FavoriteViewHolder>(MealEntityDiff()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        return FavoriteViewHolder(
            ListViewHolderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        ) { transitionView, position, transitionName ->
            onItemClickListener(transitionView, getItem(position), transitionName)
        }
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class FavoriteViewHolder(
        private val binding: ListViewHolderBinding,
        private val onItemClickedPosition: (View, Int, String) -> Any
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            showShimmer(true)
            itemView.setOnClickListener {
                onItemClickedPosition(
                    binding.viewHolderBackground,
                    adapterPosition,
                    binding.viewHolderBackground.transitionName
                )
            }
        }

        fun bind(mealEntity: MealEntity) {
            binding.viewHolderTitle.text = mealEntity.name
            binding.viewHolderBackground.transitionName = mealEntity.mealId

            Glide.with(binding.root)
                .load(mealEntity.thumb)
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

class MealEntityDiff : DiffUtil.ItemCallback<MealEntity>() {
    override fun areItemsTheSame(oldItem: MealEntity, newItem: MealEntity): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: MealEntity, newItem: MealEntity): Boolean =
        areItemsTheSame(oldItem, newItem)
}