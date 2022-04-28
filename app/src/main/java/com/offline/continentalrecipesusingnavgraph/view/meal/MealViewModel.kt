package com.offline.continentalrecipesusingnavgraph.view.meal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.offline.continentalrecipesusingnavgraph.repository.MealRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MealViewModel @Inject constructor(private val mealRepositoryImpl: MealRepositoryImpl) :
    ViewModel() {

    val selectedCategoryName: String = mealRepositoryImpl.getSelectedCategory()

    val meals = liveData {
        emit(mealRepositoryImpl.getMeals(selectedCategoryName))
    }

    fun putSelectedMeal(selectedMeal: String) {
        mealRepositoryImpl.putSelectedMeal(selectedMeal)
    }
}
