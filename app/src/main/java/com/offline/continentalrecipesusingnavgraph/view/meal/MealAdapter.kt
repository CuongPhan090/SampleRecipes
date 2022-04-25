package com.offline.continentalrecipesusingnavgraph.view.meal

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
import com.offline.continentalrecipesusingnavgraph.model.Meal

class MealAdapter(val onItemClickListener: (View, Meal, String) -> Any): ListAdapter<Meal, MealAdapter.MealViewHolder>(MealDiff()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealViewHolder {
        return MealViewHolder(ListViewHolderBinding.inflate(LayoutInflater.from(parent.context), parent, false)) { transitionView, position, transitionName ->
            onItemClickListener(transitionView, getItem(position), transitionName)
        }
    }

    override fun onBindViewHolder(holder: MealViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class MealViewHolder(private val binding: ListViewHolderBinding, private val onItemClickedPosition: (View, Int, String) -> Any): RecyclerView.ViewHolder(binding.root) {

        init {
            showShimmer(true)
            itemView.setOnClickListener{
                onItemClickedPosition(binding.viewHolderBackground, adapterPosition, binding.viewHolderBackground.transitionName)
            }
        }

        fun bind(meal: Meal) {
            binding.viewHolderTitle.text = meal.name
            binding.viewHolderBackground.transitionName = meal.id

            Glide.with(binding.root)
                .load(meal.thumb)
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

class MealDiff: DiffUtil.ItemCallback<Meal>() {
    override fun areItemsTheSame(oldItem: Meal, newItem: Meal): Boolean = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: Meal, newItem: Meal): Boolean = areItemsTheSame(oldItem, newItem)
}