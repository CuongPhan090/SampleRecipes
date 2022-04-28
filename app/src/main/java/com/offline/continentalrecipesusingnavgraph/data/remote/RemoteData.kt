package com.offline.continentalrecipesusingnavgraph.data.remote

import com.offline.continentalrecipesusingnavgraph.model.Categories
import com.offline.continentalrecipesusingnavgraph.model.Meals
import com.offline.continentalrecipesusingnavgraph.model.Recipes
import javax.inject.Inject

interface RemoteData {
    suspend fun getCategories(): Categories
    suspend fun getMeals(selectedCategory: String): Meals
    suspend fun getRecipe(selectedMeal: String): Recipes
}

class RemoteDataImpl @Inject constructor(private val apiClient: ApiClient) : RemoteData {
    override suspend fun getCategories(): Categories =
        apiClient.getCategories()

    override suspend fun getMeals(selectedCategory: String): Meals =
        apiClient.getMeals(selectedCategory)

    override suspend fun getRecipe(selectedMeal: String): Recipes =
        apiClient.getRecipe(selectedMeal)

}