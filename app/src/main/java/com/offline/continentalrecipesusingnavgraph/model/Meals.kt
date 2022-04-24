package com.offline.continentalrecipesusingnavgraph.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Meals(
    val meals: List<Meal>
)

@JsonClass(generateAdapter = true)
data class Meal(
    @Json(name = "strMeal")
    val name: String,

    @Json(name = "strMealThumb")
    val thumb: String,

    @Json(name = "idMeal")
    val id: String
)