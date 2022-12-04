package com.codinginflow.foodyapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codinginflow.foodyapp.data.DataStoreRepository
import com.codinginflow.foodyapp.util.Constants.Companion.API_KEY
import com.codinginflow.foodyapp.util.Constants.Companion.DEFAULT_DIET_TYPE
import com.codinginflow.foodyapp.util.Constants.Companion.DEFAULT_MEAL_TYPE
import com.codinginflow.foodyapp.util.Constants.Companion.DEFAULT_RECIPES_NUMBER
import com.codinginflow.foodyapp.util.Constants.Companion.QUERY_ADD_RECIPE_INFORMATION
import com.codinginflow.foodyapp.util.Constants.Companion.QUERY_API_KEY
import com.codinginflow.foodyapp.util.Constants.Companion.QUERY_DIET
import com.codinginflow.foodyapp.util.Constants.Companion.QUERY_FILL_INGREDIENTS
import com.codinginflow.foodyapp.util.Constants.Companion.QUERY_NUMBER
import com.codinginflow.foodyapp.util.Constants.Companion.QUERY_SEARCH
import com.codinginflow.foodyapp.util.Constants.Companion.QUERY_TYPE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipesViewModel @Inject constructor(
    private val dataStoreRepository: DataStoreRepository
) : ViewModel() {

    private var mealType = DEFAULT_MEAL_TYPE
    private var dietType = DEFAULT_DIET_TYPE

    val readMealAndDietType = dataStoreRepository.readMealAndDietType

    var networkStatus = false

    fun saveMealAndDietType(mealType: String, mealTypeId: Int, dietType: String, dietTypeId: Int) =
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.saveMealAndDietType(mealType, mealTypeId, dietType, dietTypeId)
        }

    fun applyQueries(): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()

        queries[QUERY_NUMBER] = DEFAULT_RECIPES_NUMBER
        queries[QUERY_API_KEY] = API_KEY
        queries[QUERY_TYPE] = mealType
        queries[QUERY_DIET] = dietType
        queries[QUERY_ADD_RECIPE_INFORMATION] = "true"
        queries[QUERY_FILL_INGREDIENTS] = "true"
        return queries
    }

    fun applyOrderFood(){
        viewModelScope.launch {
            readMealAndDietType.collect { value ->
                mealType = value.selectedMealType
                dietType = value.selectedDietType
            }
        }
    }

    fun applySearchQuery(searchQuery: String): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()

        queries[QUERY_SEARCH] = searchQuery
        queries[QUERY_NUMBER] = DEFAULT_RECIPES_NUMBER
        queries[QUERY_API_KEY] = API_KEY
        queries[QUERY_ADD_RECIPE_INFORMATION] = "true"
        queries[QUERY_FILL_INGREDIENTS] = "true"
        return queries
    }

}