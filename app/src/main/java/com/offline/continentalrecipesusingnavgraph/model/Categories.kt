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
    val name: String,

    @Json(name = "strCategoryThumb")
    val thumb: String,

    @Json(name = "strCategoryDescription")
    val description: String
)
