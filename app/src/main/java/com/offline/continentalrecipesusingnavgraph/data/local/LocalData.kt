package com.offline.continentalrecipesusingnavgraph.data.local

import androidx.lifecycle.LiveData
import javax.inject.Inject

interface LocalData {
    suspend fun insertMeal(meal: MealEntity)

    suspend fun removeMeal(meal: MealEntity)

    fun getAllMeals(): LiveData<List<MealEntity>>
}

class LocalDataImpl @Inject constructor(private val mealDao: MealDao): LocalData {
    override suspend fun insertMeal(meal: MealEntity) {
        mealDao.insertMeal(meal)
    }

    override suspend fun removeMeal(meal: MealEntity) {
        mealDao.removeMeal(meal)
    }

    override fun getAllMeals(): LiveData<List<MealEntity>> =
        mealDao.getAllMeals()
}