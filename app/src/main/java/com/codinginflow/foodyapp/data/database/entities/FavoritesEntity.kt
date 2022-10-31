package com.codinginflow.foodyapp.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.codinginflow.foodyapp.model.ResultRecipe
import com.codinginflow.foodyapp.util.Constants.Companion.FAVORITE_RECIPES_TABLE

@Entity(tableName = FAVORITE_RECIPES_TABLE)
class FavoritesEntity(
    @PrimaryKey(autoGenerate = false) var id: Int, var result: ResultRecipe
)