package com.offline.continentalrecipesusingnavgraph.repository

import androidx.lifecycle.LiveData
import com.offline.continentalrecipesusingnavgraph.data.local.LocalDataImpl
import com.offline.continentalrecipesusingnavgraph.data.local.MealEntity
import com.offline.continentalrecipesusingnavgraph.data.remote.RemoteDataImpl
import com.offline.continentalrecipesusingnavgraph.model.Categories
import com.offline.continentalrecipesusingnavgraph.model.Meals
import com.offline.continentalrecipesusingnavgraph.model.Recipes
import javax.inject.Inject
import javax.inject.Singleton

interface MealRepository {
    suspend fun getCategories(): Categories?
    fun putSelectedCategory(selectedCategoryName: String)
    fun getSelectedCategory(): String

    suspend fun getMeals(selectedCategory: String): Meals?
    fun putSelectedMeal(selectedMealName: String)
    fun getSelectedMeal(): String

    suspend fun getRecipe(selectedMealName: String): Recipes?

    suspend fun insertMeal(meal: MealEntity)
    suspend fun removeMeal(meal: MealEntity)
    fun getAllFavoriteMeals(): LiveData<List<MealEntity>>
}

@Singleton
class MealRepositoryImpl @Inject constructor(
    private val remoteData: RemoteDataImpl,
    private val localData: LocalDataImpl
) : MealRepository {

    private val itemClickedMap = mutableMapOf<String, String>()

    override suspend fun getCategories(): Categories? =
        remoteData.getCategories().body

    override fun putSelectedCategory(selectedCategoryName: String) {
        itemClickedMap["selectedCategoryName"] = selectedCategoryName
    }

    override fun getSelectedCategory(): String =
        itemClickedMap["selectedCategoryName"] ?: ""

    override suspend fun getMeals(selectedCategory: String): Meals? =
        remoteData.getMeals(selectedCategory).body

    override fun putSelectedMeal(selectedMealName: String) {
        itemClickedMap["selectedMeal"] = selectedMealName
    }

    override fun getSelectedMeal(): String =
        itemClickedMap["selectedMeal"] ?: ""

    override suspend fun getRecipe(selectedMealName: String): Recipes? =
        remoteData.getRecipe(selectedMealName).body

    override suspend fun insertMeal(meal: MealEntity) =
        localData.insertMeal(meal)

    override suspend fun removeMeal(meal: MealEntity) =
        localData.removeMeal(meal)

    override fun getAllFavoriteMeals(): LiveData<List<MealEntity>> =
        localData.getAllMeals()
}