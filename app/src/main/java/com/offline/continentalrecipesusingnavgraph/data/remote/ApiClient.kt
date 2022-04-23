package com.offline.continentalrecipesusingnavgraph.data.remote

import com.offline.continentalrecipesusingnavgraph.model.Categories
import retrofit2.http.GET

interface ApiClient {

    @GET("categories.php")
    suspend fun getCategories(): Categories

}