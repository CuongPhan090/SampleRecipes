package com.offline.continentalrecipesusingnavgraph.view.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.offline.continentalrecipesusingnavgraph.repository.MealRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(private val mealRepositoryImpl: MealRepositoryImpl): ViewModel() {

    val category = liveData {
        emit(mealRepositoryImpl.getCategories())
    }

    fun putSelectedCategory(selectedCategoryName: String) {
        mealRepositoryImpl.putSelectedCategory(selectedCategoryName)
    }
}