package com.offline.continentalrecipesusingnavgraph.data.remote

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.offline.continentalrecipesusingnavgraph.data.model.FavoriteRecipe
import com.offline.continentalrecipesusingnavgraph.data.model.User

interface FirebaseApi {

    suspend fun createUserAccount(uid: String, user: User)
    suspend fun addFavouriteRecipe(uid: String, favoriteRecipe: FavoriteRecipe)
    suspend fun removeFavouriteRecipe(uid: String, favoriteRecipe: FavoriteRecipe)
    suspend fun getFavoriteRecipes(uid: String): List<FavoriteRecipe>
    fun setDataChangeListener(uid: String)
}

class FirebaseApiImpl: FirebaseApi {
    private val firebaseDatabase = Firebase.database.reference
    private var favouriteRecipes = mutableListOf<FavoriteRecipe>()
    private val favouriteRecipesListener = object: ValueEventListener{
        override fun onDataChange(snapshot: DataSnapshot) {
            snapshot.getValue<MutableList<FavoriteRecipe>>()?.let {
                favouriteRecipes = it
            }
        }
        override fun onCancelled(error: DatabaseError) {}
    }

    override suspend fun createUserAccount(uid: String, user: User) {
        firebaseDatabase.child("users").child(uid).setValue(user)
    }

    override suspend fun addFavouriteRecipe(uid: String, favoriteRecipe: FavoriteRecipe) {
        favouriteRecipes.add(favoriteRecipe)
        firebaseDatabase.child("favourites").child(uid).setValue(favouriteRecipes)
    }

    override suspend fun removeFavouriteRecipe(uid: String, favoriteRecipe: FavoriteRecipe) {
        favouriteRecipes.remove(favoriteRecipe)
        firebaseDatabase.child("favourites").child(uid).setValue(favouriteRecipes)
    }

    override fun setDataChangeListener(uid: String) {
        firebaseDatabase.child("favourites").child(uid).addValueEventListener(favouriteRecipesListener)
    }

    override suspend fun getFavoriteRecipes(uid: String): List<FavoriteRecipe> = favouriteRecipes
}
