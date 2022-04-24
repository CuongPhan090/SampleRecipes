package com.offline.continentalrecipesusingnavgraph.view.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.offline.continentalrecipesusingnavgraph.repository.MealRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(val mealRepositoryImpl: MealRepositoryImpl): ViewModel() {

    val allFavoriteMeals = mealRepositoryImpl.getAllFavoriteMeals()

    fun putSelectedMeal(selectedMeal: String) {
        mealRepositoryImpl.putSelectedMeal(selectedMeal)
    }
}