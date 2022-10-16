package com.codinginflow.foodyapp.databinding

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import coil.load
import com.codinginflow.foodyapp.R
import com.codinginflow.foodyapp.model.ResultRecipe
import com.codinginflow.foodyapp.ui.mainfragments.favorite.FavoriteRecipesFragmentDirections
import com.codinginflow.foodyapp.ui.mainfragments.recipes.RecipesFragmentDirections
import com.codinginflow.foodyapp.util.Constants.Companion.BASE_IMAGE_URL
import org.jsoup.Jsoup

class RecipesRowBinding {

    companion object {

        @BindingAdapter("onRecipeClickListener")
        @JvmStatic
        fun onRecipeClickListener(recipeRowLayout: ConstraintLayout, result: ResultRecipe) {
            recipeRowLayout.setOnClickListener {
                try {
                    val action =
                        RecipesFragmentDirections.actionRecipesFragmentToDetailsActivity(result)
                    recipeRowLayout.findNavController().navigate(action)
                } catch (e: Exception) {
                    Log.e("onRecipeClickListener" , e.toString())
                }
            }
        }


        @BindingAdapter("android:loadImageFromUrl")
        @JvmStatic
        fun loadImageFromUrl(imageView: ImageView, imageUrl: String) {
            imageView.load(imageUrl) {
                crossfade(600)
                error(R.drawable.ic_image_search)
            }
        }

        @BindingAdapter("android:ingredientsLoadImageFromUrl")
        @JvmStatic
        fun ingredientsLoadImageFromUrl(imageView: ImageView, url: String?) {
            val imageUrl = BASE_IMAGE_URL + url
            try {
                imageView.load(imageUrl) {
                    crossfade(600)
                    error(R.drawable.ic_image_search)
                }
            } catch (e: Exception) {
                Log.e("ingredientsLoadImageFromUrl", "CANT LOAD IMAGE")
            }
        }

        @BindingAdapter("android:setNumberOfLikes")
        @JvmStatic
        fun setNumberOfLike(textView: TextView, likes: Int) {
            textView.text = likes.toString()
        }

        @BindingAdapter("android:setNumberOfMinutes")
        @JvmStatic
        fun setNumberOfMinutes(textView: TextView, minutes: Int) {
            textView.text = minutes.toString()
        }

        @BindingAdapter("android:setNumberOfAmount")
        @JvmStatic
        fun setNumberOfAmount(textView: TextView, amount: Double) {
            textView.text = amount.toString()
        }

        @BindingAdapter("android:applyColor")
        @JvmStatic
        fun applyColor(view: View, vegan: Boolean) {
            if (vegan) {
                when (view) {
                    is TextView -> {
                        view.setTextColor(ContextCompat.getColor(view.context, R.color.green))
                    }
                    is ImageView -> {
                        view.setColorFilter(ContextCompat.getColor(view.context, R.color.green))
                    }
                }
            }
        }

        @BindingAdapter("android:parseHtml")
        @JvmStatic
        fun parseHtml(textView: TextView, description: String?) {
            if (description != null) {
                val desc = Jsoup.parse(description).text()
                textView.text = desc
            }
        }


    }
}