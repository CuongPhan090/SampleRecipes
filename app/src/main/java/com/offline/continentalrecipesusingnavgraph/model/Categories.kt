package com.offline.continentalrecipesusingnavgraph.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Categories(
    val categories: List<Category>
)

@JsonClass(generateAdapter = true)
data class Category(
    @Json(name = "idCategory")
    val id: String,

    @Json(name = "strCategory")
    val categoryName: String,

    @Json(name = "strCategoryThumb")
    val categoryThumb: String,

    @Json(name = "strCategoryDescription")
    val categoryDescription: String
)
