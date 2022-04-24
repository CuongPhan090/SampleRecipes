package com.offline.continentalrecipesusingnavgraph.repository

import com.offline.continentalrecipesusingnavgraph.data.remote.RemoteDataImpl
import com.offline.continentalrecipesusingnavgraph.model.Categories
import javax.inject.Inject

interface MealRepository {
    suspend fun getCategory(): Categories
}

class MealRepositoryImpl @Inject constructor(private val remoteData: RemoteDataImpl): MealRepository{

    override suspend fun getCategory(): Categories = remoteData.getCategory()
}