package com.codinginflow.foodyapp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.codinginflow.foodyapp.R
import com.codinginflow.foodyapp.databinding.RecipesRowLayoutBinding
import com.codinginflow.foodyapp.model.FoodRecipe
import com.codinginflow.foodyapp.model.ResultRecipe
import com.codinginflow.foodyapp.util.RecipesDiffUtil

class RecipesAdapter : RecyclerView.Adapter<RecipesAdapter.MyViewHolder>() {

    var recipes: List<ResultRecipe> = emptyList()

    class MyViewHolder(private val binding: RecipesRowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(result: ResultRecipe) {
            binding.result = result
            binding.executePendingBindings() // Live
        }

        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RecipesRowLayoutBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val resultRecipe = recipes[position]
        holder.bind(resultRecipe)

        val likedImage = holder.itemView.findViewById<ImageView>(R.id.rrHeartImage)
        val likedTitle = holder.itemView.findViewById<TextView>(R.id.rrHeartTitle)
        likedImage.setOnClickListener {
            if (likedTitle.text == resultRecipe.aggregateLikes.toString()) {
                likedImage.setImageResource(R.drawable.ic_heart)
                likedTitle.text =
                    (resultRecipe.aggregateLikes + 1).toString()
            } else {
                likedImage.setImageResource(R.drawable.ic_heart_broken)
                likedTitle.text =
                    resultRecipe.aggregateLikes.toString()
            }
        }
    }

    override fun getItemCount(): Int {
        return recipes.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(newData: FoodRecipe) {
        val recipesDiffUtil = RecipesDiffUtil(recipes, newData.results)
        val diffUtilResult = DiffUtil.calculateDiff(recipesDiffUtil)
        recipes = newData.results
        diffUtilResult.dispatchUpdatesTo(this)
    }
}