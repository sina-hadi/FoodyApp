package com.codinginflow.foodyapp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.codinginflow.foodyapp.databinding.IngredientsRowLayoutBinding
import com.codinginflow.foodyapp.model.ExtendedIngredient
import com.codinginflow.foodyapp.model.ResultRecipe
import com.codinginflow.foodyapp.util.RecipesDiffUtil

class IngredientsAdapter : RecyclerView.Adapter<IngredientsAdapter.MyViewHolder>() {

    private var ingredientsList = emptyList<ExtendedIngredient>()

    class MyViewHolder(private val binding: IngredientsRowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(result: ExtendedIngredient) {
            binding.ingredients = result
            binding.executePendingBindings() // Live
        }

        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = IngredientsRowLayoutBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val resultIngredient = ingredientsList[position]
        holder.bind(resultIngredient)
    }

    override fun getItemCount(): Int {
        return ingredientsList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(newData: ResultRecipe) {
        val recipesDiffUtil = RecipesDiffUtil(ingredientsList, newData.extendedIngredients)
        val diffUtilResult = DiffUtil.calculateDiff(recipesDiffUtil)
        ingredientsList = newData.extendedIngredients
        diffUtilResult.dispatchUpdatesTo(this)
    }
}