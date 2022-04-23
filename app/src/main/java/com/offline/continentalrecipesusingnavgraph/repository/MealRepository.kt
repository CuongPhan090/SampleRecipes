package com.offline.continentalrecipesusingnavgraph.repository

import com.offline.continentalrecipesusingnavgraph.data.remote.RemoteData
import com.offline.continentalrecipesusingnavgraph.model.Categories
import javax.inject.Inject

interface MealRepository {
    suspend fun getCategory(): Categories
}

class MealRepositoryImpl @Inject constructor(private val remoteData: RemoteData): MealRepository{

    override suspend fun getCategory(): Categories = remoteData.getCategory()
}