package com.offline.continentalrecipesusingnavgraph.view.recipe

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.firebase.auth.FirebaseAuth
import com.offline.continentalrecipesusingnavgraph.R
import com.offline.continentalrecipesusingnavgraph.data.local.MealEntity
import com.offline.continentalrecipesusingnavgraph.data.model.FavoriteRecipe
import com.offline.continentalrecipesusingnavgraph.data.remote.FirebaseApiImpl
import com.offline.continentalrecipesusingnavgraph.databinding.FragmentRecipeBinding
import com.offline.continentalrecipesusingnavgraph.model.Recipe
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RecipeFragment : Fragment() {
    private lateinit var binding: FragmentRecipeBinding
    private val viewModel: RecipeViewModel by viewModels()
    private lateinit var favoriteMeal: List<MealEntity>
    private val args: RecipeFragmentArgs by navArgs()
    private val firebaseApi = FirebaseApiImpl()
    private val firebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val transition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.move)
        sharedElementEnterTransition = transition
        sharedElementReturnTransition = transition
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRecipeBinding.inflate(layoutInflater)
        // this check whether fragment is opened from deeplink?
        args.name?.let {
            viewModel.selectedMeal = it
        }
        (activity as AppCompatActivity).supportActionBar?.title = viewModel.selectedMeal
        postponeEnterTransition()

        firebaseApi.setDataChangeListener(firebaseAuth.currentUser?.uid ?: "")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.favoriteMeals.observe(viewLifecycleOwner) {
            favoriteMeal = it
        }
        viewModel.recipe.observe(viewLifecycleOwner) {
            val recipe = it.recipes[0]
            setupView(recipe)
            binding.recipeImage.transitionName = recipe.idMeal
            binding.favoriteFab.setOnClickListener {
                val mealEntity: MealEntity = getExistEntityOrNewOne(recipe)
                if (isFavorite(recipe)) {
                    binding.favoriteFab.setImageDrawable(
                        getDrawable(
                            binding.root.context,
                            R.drawable.ic_hollow_favorite
                        )
                    )
                    viewModel.removeFavoriteMeal(mealEntity)
                    CoroutineScope(Dispatchers.IO).launch {
                        firebaseApi.removeFavouriteRecipe(firebaseAuth.currentUser?.uid ?: "", FavoriteRecipe(name = recipe.name, thumb = recipe.thumb, id = recipe.idMeal))
                    }
                } else {
                    binding.favoriteFab.setImageDrawable(
                        getDrawable(
                            binding.root.context,
                            R.drawable.ic_dense_favorite
                        )
                    )
                    viewModel.addFavoriteMeal(mealEntity)
                    CoroutineScope(Dispatchers.IO).launch {
                        firebaseApi.addFavouriteRecipe(firebaseAuth.currentUser?.uid ?: "", FavoriteRecipe(name = recipe.name, thumb = recipe.thumb, id = recipe.idMeal))
                    }
                }
            }
        }
    }


    private fun getExistEntityOrNewOne(recipe: Recipe): MealEntity {
        favoriteMeal.forEach {
            if (it.name == recipe.name) {
                return it
            }
        }
        return MealEntity(0, recipe.name ?: "", recipe.thumb ?: "", recipe.idMeal ?: "")
    }

    private fun isFavorite(currentRecipe: Recipe): Boolean {
        favoriteMeal.forEach { mealEntity ->
            if (currentRecipe.name == mealEntity.name) {
                return true
            }
        }
        return false
    }

    private fun setupView(recipe: Recipe) {
        loadImage(recipe.thumb)
        updateFavoriteFab(recipe)
        setupIngredients(recipe)
        setupInstructions(recipe)
    }

    private fun updateFavoriteFab(recipe: Recipe) {
        if (isFavorite(recipe)) {
            binding.favoriteFab.setImageDrawable(
                getDrawable(
                    binding.root.context,
                    R.drawable.ic_dense_favorite
                )
            )
        } else {
            binding.favoriteFab.setImageDrawable(
                getDrawable(
                    binding.root.context,
                    R.drawable.ic_hollow_favorite
                )
            )

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
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    startPostponedEnterTransition()
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    startPostponedEnterTransition()
                    return false
                }
            })
            .into(binding.recipeImage)
    }
}