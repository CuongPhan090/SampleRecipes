package com.offline.continentalrecipesusingnavgraph.view.meal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.offline.continentalrecipesusingnavgraph.R
import com.offline.continentalrecipesusingnavgraph.databinding.FragmentMealBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MealFragment : Fragment() {
    lateinit var binding: FragmentMealBinding
    lateinit var adapter: MealAdapter
    private val viewModel: MealViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMealBinding.inflate(layoutInflater, container, false)
        (activity as AppCompatActivity).supportActionBar?.title = viewModel.selectedCategoryName

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapter()

        postponeEnterTransition()
        view.doOnPreDraw {
            startPostponedEnterTransition()
        }
    }

    private fun setupAdapter() {
        adapter = MealAdapter { transitionView, meal, transitionNameOfNextFragment ->
            viewModel.putSelectedMeal(meal.name)
            findNavController().navigate(
                R.id.action_mealFragment_to_recipeFragment,
                null,
                null,
                FragmentNavigatorExtras(transitionView to transitionNameOfNextFragment)
            )
        }

        binding.mealRecyclerView.adapter = adapter
        viewModel.meals.observe(viewLifecycleOwner) {
            adapter.submitList(it?.meals)
        }
    }
}