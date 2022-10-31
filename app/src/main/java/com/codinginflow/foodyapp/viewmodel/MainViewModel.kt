package com.codinginflow.foodyapp.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.codinginflow.foodyapp.data.database.entities.FavoritesEntity
import com.codinginflow.foodyapp.data.database.entities.FoodJokeEntity
import com.codinginflow.foodyapp.data.database.entities.RecipesEntity
import com.codinginflow.foodyapp.data.network.Repository
import com.codinginflow.foodyapp.model.FoodJoke
import com.codinginflow.foodyapp.model.FoodRecipe
import com.codinginflow.foodyapp.util.HandleResponse
import com.codinginflow.foodyapp.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository,
    application: Application
) : AndroidViewModel(application) {

    // ROOM
    val readRecipes: LiveData<List<RecipesEntity>> = repository.local.readRecipes().asLiveData()
    val readFavoriteRecipes : LiveData<List<FavoritesEntity>> = repository.local.readFavoriteRecipes().asLiveData()
    val readFoodJoke: LiveData<List<FoodJokeEntity>> = repository.local.readFoodJoke().asLiveData()

    private fun insertRecipes(recipesEntity: RecipesEntity) =
        viewModelScope.launch (Dispatchers.IO) {
            repository.local.insertRecipes(recipesEntity)
        }

    fun insertFavoriteRecipe(favoritesEntity: FavoritesEntity) =
        viewModelScope.launch (Dispatchers.IO) {
            repository.local.insertFavoriteRecipe(favoritesEntity)
        }

    fun insertFoodJoke(foodJokeEntity: FoodJokeEntity) =
        viewModelScope.launch (Dispatchers.IO) {
            repository.local.insertFoodJoke(foodJokeEntity)
        }

    fun deleteFavoriteRecipe(id: Int) =
        viewModelScope.launch (Dispatchers.IO) {
            repository.local.deleteFavoriteRecipe(id)
        }

    // RETROFIT
    var recipesResponse: MutableLiveData<NetworkResult<FoodRecipe>> = MutableLiveData()
    var searchedRecipesResponse: MutableLiveData<NetworkResult<FoodRecipe>> = MutableLiveData()
    val foodJokeResponse: MutableLiveData<NetworkResult<FoodJoke>> = MutableLiveData()

    fun getRecipes(queries: Map<String, String>) = viewModelScope.launch {
        getRecipesSafeCall(queries)
    }

    fun searchRecipes(searchQuery: Map<String, String>) = viewModelScope.launch {
        searchRecipesSafeCall(searchQuery)
    }

    fun getFoodJoke(apiKey: String) = viewModelScope.launch {
        getFoodJokeSafeCall(apiKey)
    }

    private suspend fun getFoodJokeSafeCall(apikey: String) {
        foodJokeResponse.value = NetworkResult.Loading()
        try {
            val response = repository.remote.getFoodJoke(apikey)
            foodJokeResponse.value = HandleResponse(response).handleResponse

            val foodJoke = foodJokeResponse.value!!.data
            if (foodJoke != null) {
                offlineCacheFoodJoke(foodJoke)
            }

        } catch (e: Exception) {
            foodJokeResponse.value = NetworkResult.Error("Joke not found.")
        }
    }

    private suspend fun getRecipesSafeCall(queries: Map<String, String>) {
        recipesResponse.value = NetworkResult.Loading()
        try {
            Log.e("ABCD MAin", "1")
            val response = repository.remote.getRecipes(queries)
            Log.e("ABCD MAin", "2")
            recipesResponse.value = HandleResponse(response).handleResponse
            Log.e("ABCD MAin", "3")

            val foodRecipe = recipesResponse.value!!.data
            if (foodRecipe != null) {
                offlineCacheRecipes(foodRecipe)
            }

        } catch (e: Exception) {
            Log.e("ABCD MAin", "4")
            recipesResponse.value = NetworkResult.Error("Recipes not found.")
        }
    }

    private suspend fun searchRecipesSafeCall(searchQuery: Map<String, String>) {
        searchedRecipesResponse.value = NetworkResult.Loading()
        try {
            val response = repository.remote.searchRecipes(searchQuery)
            searchedRecipesResponse.value = HandleResponse(response).handleResponse

        } catch (e: Exception) {
            searchedRecipesResponse.value = NetworkResult.Error("Recipes not found.")
        }
    }

    private fun offlineCacheRecipes(foodRecipe: FoodRecipe) {
        val recipesEntity = RecipesEntity(foodRecipe)
        insertRecipes(recipesEntity)
    }

    private fun offlineCacheFoodJoke(foodJoke: FoodJoke) {
        val foodJokeEntity = FoodJokeEntity(foodJoke)
        insertFoodJoke(foodJokeEntity)
    }
}