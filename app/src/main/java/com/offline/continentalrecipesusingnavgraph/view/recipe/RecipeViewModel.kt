package com.offline.continentalrecipesusingnavgraph.view.recipe

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.offline.continentalrecipesusingnavgraph.repository.MealRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RecipeViewModel @Inject constructor(private val mealRepositoryImpl: MealRepositoryImpl): ViewModel() {

    val selectedMeal = mealRepositoryImpl.getSelectedMeal()

    val recipe = liveData {
        emit(mealRepositoryImpl.getRecipe(selectedMeal))
    }
}