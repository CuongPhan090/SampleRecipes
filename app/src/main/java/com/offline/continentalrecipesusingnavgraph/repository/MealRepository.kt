package com.offline.continentalrecipesusingnavgraph.repository

import com.offline.continentalrecipesusingnavgraph.data.remote.RemoteDataImpl
import com.offline.continentalrecipesusingnavgraph.model.Categories
import com.offline.continentalrecipesusingnavgraph.model.Meals
import javax.inject.Inject
import javax.inject.Singleton

interface MealRepository {
    suspend fun getCategories(): Categories
    fun putSelectedCategory(selectedCategoryName: String)
    fun getSelectedCategory(): String

    suspend fun getMeals(selectedCategory: String): Meals
    fun putSelectedMeal(selectedMealName: String)
    fun getSelectedMeal(): String
}

@Singleton
class MealRepositoryImpl @Inject constructor(private val remoteData: RemoteDataImpl): MealRepository{

    private val itemClickedMap = mutableMapOf<String, String>()

    override suspend fun getCategories(): Categories =
        remoteData.getCategories()

    override fun putSelectedCategory(selectedCategoryName: String) {
        itemClickedMap["selectedCategoryName"] = selectedCategoryName
    }

    override fun getSelectedCategory(): String =
        itemClickedMap["selectedCategoryName"] ?: ""

    override suspend fun getMeals(selectedCategory: String): Meals =
        remoteData.getMeals(selectedCategory)

    override fun putSelectedMeal(selectedMealName: String) {
        itemClickedMap["selectedMeal"] = selectedMealName
    }

    override fun getSelectedMeal(): String =
        itemClickedMap["selectedMeal"] ?: ""
}