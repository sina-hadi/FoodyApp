package com.codinginflow.foodyapp.databinding

import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.databinding.BindingAdapter
import com.codinginflow.foodyapp.data.database.entities.FoodJokeEntity
import com.codinginflow.foodyapp.data.database.entities.RecipesEntity
import com.codinginflow.foodyapp.model.FoodJoke
import com.codinginflow.foodyapp.model.FoodRecipe
import com.codinginflow.foodyapp.util.NetworkResult
import com.google.android.material.card.MaterialCardView

class RecipesBinding {

    companion object {

        @BindingAdapter("eReadApiResponse")
        @JvmStatic
        fun errorImageViewVisibility(
            linearLayout: LinearLayout, apiResponse: NetworkResult<FoodRecipe>?
        ) {
            when (apiResponse) {
                is NetworkResult.Error -> {
                    linearLayout.visibility = View.VISIBLE
                }
                is NetworkResult.Loading -> {
                    linearLayout.visibility = View.INVISIBLE
                }
                is NetworkResult.Success -> {
                    linearLayout.visibility = View.INVISIBLE
                }
                else -> {}
            }
        }

        @BindingAdapter("lReadApiResponse")
        @JvmStatic
        fun loadingImageViewVisibility(
            linearLayout: LinearLayout, apiResponse: NetworkResult<FoodRecipe>?
        ) {
            when (apiResponse) {
                is NetworkResult.Error -> {
                    linearLayout.visibility = View.INVISIBLE
                }
                is NetworkResult.Loading -> {
                    linearLayout.visibility = View.VISIBLE
                }
                is NetworkResult.Success -> {
                    linearLayout.visibility = View.INVISIBLE
                }
                else -> {}
            }
        }

        @BindingAdapter("readApiResponseJoke", "readDatabaseJoke", requireAll = false)
        @JvmStatic
        fun setCardAndProgressVisibility(
            view: View, apiResponse: NetworkResult<FoodJoke>?, database: List<FoodJokeEntity>?
        ) {
            when (apiResponse) {
                is NetworkResult.Loading -> {
                    when (view) {
                        is ProgressBar -> {
                            view.visibility = View.VISIBLE
                        }
                        is MaterialCardView -> {
                            view.visibility = View.INVISIBLE
                        }
                    }
                }
                is NetworkResult.Error -> {
                    when (view) {
                        is ProgressBar -> {
                            view.visibility = View.INVISIBLE
                        }
                        is MaterialCardView -> {
                            view.visibility = View.VISIBLE
                            if (database != null && database.isEmpty()) {
                                view.visibility = View.INVISIBLE
                            }
                        }
                    }
                }
                is NetworkResult.Success -> {
                    when (view) {
                        is ProgressBar -> {
                            view.visibility = View.INVISIBLE
                        }
                        is MaterialCardView -> {
                            view.visibility = View.VISIBLE
                        }
                    }
                }
                null -> {}
            }
        }
    }
}