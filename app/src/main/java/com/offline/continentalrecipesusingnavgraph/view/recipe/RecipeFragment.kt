package com.offline.continentalrecipesusingnavgraph.view.recipe

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.offline.continentalrecipesusingnavgraph.R
import com.offline.continentalrecipesusingnavgraph.databinding.FragmentRecipeBinding
import com.offline.continentalrecipesusingnavgraph.model.Recipe
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecipeFragment : Fragment() {
    lateinit var binding: FragmentRecipeBinding
    private val viewModel: RecipeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRecipeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.recipe.observe(viewLifecycleOwner) {
            val recipe = it.recipes[0]
            loadImage(recipe.thumb)

            setupIngredients(recipe)
            setupInstructions(recipe)
        }

        binding.favoriteFab.setOnClickListener{
            // save meal to favorite
        }
    }

    private fun setupInstructions(recipe: Recipe) {
        binding.instructions.recipeDetailCardTitle.text = getString(R.string.instructions)
        binding.instructions.recipeDetailCardContent.text = recipe.instruction
    }

    private fun setupIngredients(recipe: Recipe) {
        binding.ingredients.recipeDetailCardTitle.text = getString(R.string.ingredients)
        binding.ingredients.recipeDetailCardContent.text = parseIngredient(recipe)
    }

    private fun parseIngredient(recipe: Recipe): String {
        val ingredientAndMeasurement = mapOf(
            recipe.strIngredient1 to recipe.strMeasure1,
            recipe.strIngredient2 to recipe.strMeasure2,
            recipe.strIngredient3 to recipe.strMeasure3,
            recipe.strIngredient4 to recipe.strMeasure4,
            recipe.strIngredient5 to recipe.strMeasure5,
            recipe.strIngredient6 to recipe.strMeasure6,
            recipe.strIngredient7 to recipe.strMeasure7,
            recipe.strIngredient8 to recipe.strMeasure8,
            recipe.strIngredient9 to recipe.strMeasure9,
            recipe.strIngredient10 to recipe.strMeasure10,
            recipe.strIngredient11 to recipe.strMeasure11,
            recipe.strIngredient12 to recipe.strMeasure12,
            recipe.strIngredient13 to recipe.strMeasure13,
            recipe.strIngredient14 to recipe.strMeasure14,
            recipe.strIngredient15 to recipe.strMeasure15,
            recipe.strIngredient16 to recipe.strMeasure16,
            recipe.strIngredient17 to recipe.strMeasure17,
            recipe.strIngredient18 to recipe.strMeasure18,
            recipe.strIngredient19 to recipe.strMeasure19,
            recipe.strIngredient20 to recipe.strMeasure20,
        )

        var ingredients = ""
        for (pair in ingredientAndMeasurement.entries) {
            if (pair.key.isNullOrEmpty()) {
                break
            }
            ingredients += "${pair.value} ${pair.key}  \n"
        }
        return ingredients
    }

    private fun loadImage(url: String?) {
        Glide.with(this)
            .load(url)
            .into(binding.recipeImage)
    }
}