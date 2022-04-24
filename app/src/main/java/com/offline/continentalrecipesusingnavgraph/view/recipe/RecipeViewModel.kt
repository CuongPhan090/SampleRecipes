package com.offline.continentalrecipesusingnavgraph.view.recipe

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.offline.continentalrecipesusingnavgraph.data.local.MealEntity
import com.offline.continentalrecipesusingnavgraph.repository.MealRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeViewModel @Inject constructor(private val mealRepositoryImpl: MealRepositoryImpl): ViewModel() {

    val selectedMeal = mealRepositoryImpl.getSelectedMeal()

    val recipe = liveData {
        emit(mealRepositoryImpl.getRecipe(selectedMeal))
    }

    val favoriteMeals = mealRepositoryImpl.getAllFavoriteMeals()

    fun addFavoriteMeal(meal: MealEntity) = viewModelScope.launch {
//        favoriteMeals.value?.forEach{
//            if (meal.name == it.name) {
//                return@launch
//            }
//        }
        mealRepositoryImpl.insertMeal(meal)
    }

    fun removeFavoriteMeal(meal: MealEntity) = viewModelScope.launch {
        mealRepositoryImpl.removeMeal(meal)
    }
}