package com.codinginflow.foodyapp.model


import com.google.gson.annotations.SerializedName

data class FoodRecipe(
    @SerializedName("results")
    val results: List<ResultRecipe>
)