package com.offline.continentalrecipesusingnavgraph.data.local

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface MealDao {

    @Query("SELECT * FROM meals")
    fun getAllMeals(): LiveData<List<MealEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeal(meal: MealEntity)

    @Delete
    suspend fun removeMeal(meal: MealEntity)
}