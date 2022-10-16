package com.codinginflow.foodyapp.data.database

import androidx.room.TypeConverter
import com.codinginflow.foodyapp.model.FoodRecipe
import com.codinginflow.foodyapp.model.ResultRecipe
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class RecipesTypeConverter {

    private val gson = Gson()

    @TypeConverter
    fun foodRecipeToString(foodRecipe: FoodRecipe) : String {
        return gson.toJson(foodRecipe)
    }

    @TypeConverter
    fun stringToFoodRecipe(data: String): FoodRecipe {
        val listType = object : TypeToken<FoodRecipe>() {}.type
        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun resultToString(resultRecipe: ResultRecipe): String {
        return gson.toJson(resultRecipe)
    }

    @TypeConverter
    fun stringToResult(data: String): ResultRecipe {
        val listType = object : TypeToken<ResultRecipe>() {}.type
        return gson.fromJson(data, listType)
    }

}