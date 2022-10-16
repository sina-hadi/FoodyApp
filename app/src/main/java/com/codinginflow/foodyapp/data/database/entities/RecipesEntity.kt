package com.codinginflow.foodyapp.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.codinginflow.foodyapp.model.FoodRecipe
import com.codinginflow.foodyapp.util.Constants.Companion.RECIPES_TABLE

@Entity(tableName = RECIPES_TABLE)
class RecipesEntity (
    var foodRecipe: FoodRecipe
) {
    @PrimaryKey(autoGenerate = false)
    var id: Int = 0
}