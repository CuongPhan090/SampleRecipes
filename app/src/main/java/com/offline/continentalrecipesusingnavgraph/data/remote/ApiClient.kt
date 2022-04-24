package com.offline.continentalrecipesusingnavgraph.data.remote

import com.offline.continentalrecipesusingnavgraph.model.Categories
import com.offline.continentalrecipesusingnavgraph.model.Meals
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiClient {

    @GET("categories.php")
    suspend fun getCategories(): Categories

    @GET("filter.php")
    suspend fun getMeals(@Query("c") selectedCategory: String): Meals
}