package com.offline.continentalrecipesusingnavgraph.data.remote

import com.offline.continentalrecipesusingnavgraph.model.Categories
import com.offline.continentalrecipesusingnavgraph.model.Meals
import com.offline.continentalrecipesusingnavgraph.model.Recipes
import retrofit2.Response
import javax.inject.Inject

interface RemoteData {
    suspend fun getCategories(): SimpleResponse<Categories>
    suspend fun getMeals(selectedCategory: String): SimpleResponse<Meals>
    suspend fun getRecipe(selectedMeal: String): SimpleResponse<Recipes>
}

class RemoteDataImpl @Inject constructor(private val apiClient: ApiClient) : RemoteData {
    override suspend fun getCategories(): SimpleResponse<Categories> =
        safeApiCall { apiClient.getCategories() }

    override suspend fun getMeals(selectedCategory: String): SimpleResponse<Meals> =
        safeApiCall { apiClient.getMeals(selectedCategory) }

    override suspend fun getRecipe(selectedMeal: String): SimpleResponse<Recipes> =
        safeApiCall { apiClient.getRecipe(selectedMeal) }

    private inline fun <T> safeApiCall(apiCall: () -> Response<T>): SimpleResponse<T> {
        return try {
            SimpleResponse.success(apiCall.invoke())
        } catch (e: Exception) {
            SimpleResponse.failure(e)
        }
    }
}