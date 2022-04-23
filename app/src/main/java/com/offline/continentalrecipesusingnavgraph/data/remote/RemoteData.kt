package com.offline.continentalrecipesusingnavgraph.data.remote

import com.offline.continentalrecipesusingnavgraph.model.Categories
import javax.inject.Inject

interface RemoteData {
    suspend fun getCategory(): Categories
}

class RemoteDataImpl @Inject constructor(private val apiClient: ApiClient): RemoteData{
    override suspend fun getCategory(): Categories =
        apiClient.getCategories()
}