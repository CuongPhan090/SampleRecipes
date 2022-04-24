package com.offline.continentalrecipesusingnavgraph.data.remote

import com.offline.continentalrecipesusingnavgraph.model.Categories
import com.offline.continentalrecipesusingnavgraph.model.Meals
import javax.inject.Inject

interface RemoteData {
    suspend fun getCategories(): Categories
    suspend fun getMeals(selectedCategory: String): Meals
}

class RemoteDataImpl @Inject constructor(private val apiClient: ApiClient): RemoteData{
    override suspend fun getCategories(): Categories =
        apiClient.getCategories()

    override suspend fun getMeals(selectedCategory: String): Meals =
        apiClient.getMeals(selectedCategory)
}