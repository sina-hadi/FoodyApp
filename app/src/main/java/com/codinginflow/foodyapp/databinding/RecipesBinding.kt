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
import kotlinx.coroutines.flow.MutableStateFlow

class RecipesBinding {

    companion object {

        @BindingAdapter("app:eReadApiResponse", "app:eReadApiDatabase", requireAll = true)
        @JvmStatic
        fun errorImageViewVisibility(
            linearLayout: LinearLayout, apiResponse: NetworkResult<FoodRecipe>?, database: Boolean
        ) {
            if (database) {
                linearLayout.visibility = View.INVISIBLE
            } else {
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
        }

        @BindingAdapter("lReadApiResponse", "lReadApiDatabase", requireAll = true)
        @JvmStatic
        fun loadingImageViewVisibility(
            linearLayout: LinearLayout, apiResponse: NetworkResult<FoodRecipe>?, database: Boolean
        ) {
            if (database) {
                linearLayout.visibility = View.INVISIBLE
            } else {
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
        }

        @BindingAdapter("readApiResponseJoke")
        @JvmStatic
        fun setCardAndProgressVisibility(
            view: View, apiResponse: NetworkResult<FoodJoke>?
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