package com.codinginflow.foodyapp.viewmodel

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
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository,
) : ViewModel() {

    // ROOM
    val readRecipes: Flow<List<RecipesEntity>> = repository.local.readRecipes()
    val readFavoriteRecipes : Flow<List<FavoritesEntity>> = repository.local.readFavoriteRecipes()
    val readFoodJoke: Flow<List<FoodJokeEntity>> = repository.local.readFoodJoke()

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
    var errorState = MutableLiveData(false)

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
            val response = repository.remote.getRecipes(queries)
            recipesResponse.value = HandleResponse(response).handleResponse

            val foodRecipe = recipesResponse.value!!.data
            if (foodRecipe != null) {
                offlineCacheRecipes(foodRecipe)
            }

        } catch (e: Exception) {
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