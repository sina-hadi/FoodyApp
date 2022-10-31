package com.codinginflow.foodyapp.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.codinginflow.foodyapp.R
import com.codinginflow.foodyapp.data.database.entities.FavoritesEntity
import com.codinginflow.foodyapp.databinding.FavoriteRowLayoutBinding
import com.codinginflow.foodyapp.model.ResultRecipe
import com.codinginflow.foodyapp.ui.mainfragments.favorite.FavoriteRecipesFragmentDirections
import com.codinginflow.foodyapp.util.RecipesDiffUtil
import com.codinginflow.foodyapp.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar

class FavoriteAdapter(
    private val requireActivity: FragmentActivity, private val mainViewModel: MainViewModel
) : RecyclerView.Adapter<FavoriteAdapter.MyViewHolder>(), ActionMode.Callback {

    var recipes: List<FavoritesEntity> = emptyList()

    private var multiSelection = false
    private var selectedRecipes = arrayListOf<FavoritesEntity>()
    private var myViewHolders = arrayListOf<MyViewHolder>()
    private lateinit var mActionMode: ActionMode
    private lateinit var rootView: View

    class MyViewHolder(private val binding: FavoriteRowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(result: ResultRecipe) {
            binding.result = result
            binding.executePendingBindings() // Live
        }

        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = FavoriteRowLayoutBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        myViewHolders.add(holder)
        rootView = holder.itemView.rootView

        val recipeRow = holder.itemView.findViewById<ConstraintLayout>(R.id.recipesRowLayout)
        val resultRecipe: FavoritesEntity = recipes[position]
        holder.bind(resultRecipe.result)

        recipeRow.setOnClickListener {
            if (multiSelection) {
                applySelection(holder, resultRecipe)
            } else {
                val action =
                    FavoriteRecipesFragmentDirections.actionFavoriteRecipesFragmentToDetailsActivity(
                        resultRecipe.result
                    )
                recipeRow.findNavController().navigate(action)
            }
        }

        recipeRow.setOnLongClickListener {
            if (!multiSelection) {
                multiSelection = true
                requireActivity.startActionMode(this)
                applySelection(holder, resultRecipe)
                true
            } else {
                true
            }
        }
    }

    override fun getItemCount(): Int {
        return recipes.size
    }

    private fun applySelection(holder: MyViewHolder, currentRecipe: FavoritesEntity) {
        if (selectedRecipes.contains(currentRecipe)) {
            selectedRecipes.remove(currentRecipe)
            changeRecipeStyle(holder, R.drawable.recipes_shape)
        } else {
            selectedRecipes.add(currentRecipe)
            changeRecipeStyle(holder, R.drawable.selected_recipe_shape)
        }
        setTitleAndUnselect()
    }

    private fun changeRecipeStyle(holder: MyViewHolder, shape: Int) {
        holder.itemView.findViewById<ConstraintLayout>(R.id.recipesRowLayout).background =
            ContextCompat.getDrawable(
                requireActivity, shape
            )
    }

    override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        mode?.menuInflater?.inflate(R.menu.favorite_contextual_menu, menu)
        applyStatusVarColor(R.color.statusBarColor)
        mActionMode = mode!!
        return true
    }

    override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        return true
    }

    override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
        if (item?.itemId == R.id.delete_favorite_menu) {
            deleteSelectedItems()
            showSnackBar("${selectedRecipes.size} Recipes removed.")
            multiSelection = false
            selectedRecipes.clear()
            mode?.finish()
        }
        return true
    }

    override fun onDestroyActionMode(mode: ActionMode?) {
        myViewHolders.forEach { holder ->
            changeRecipeStyle(holder, R.drawable.recipes_shape)
        }
        multiSelection = false
        selectedRecipes.clear()
        applyStatusVarColor(R.color.actionBarBackground)
    }

    private fun applyStatusVarColor(color: Int) {
        requireActivity.window.statusBarColor = ContextCompat.getColor(requireActivity, color)
    }

    private fun setTitleAndUnselect() {
        when (selectedRecipes.size) {
            0 -> {
                mActionMode.finish()
            }
            1 -> {
                mActionMode.title = "1 item"
            }
            else -> {
                mActionMode.title = "${selectedRecipes.size} items"
            }
        }
    }

    private fun deleteSelectedItems() {
        selectedRecipes.forEach {
            mainViewModel.deleteFavoriteRecipe(it.id)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(newData: List<FavoritesEntity>) {
        val recipesDiffUtil = RecipesDiffUtil(recipes, newData)
        val diffUtilResult = DiffUtil.calculateDiff(recipesDiffUtil)
        recipes = newData
        diffUtilResult.dispatchUpdatesTo(this)
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(
            rootView, message, Snackbar.LENGTH_SHORT
        ).setAction("Okay") {}.show()
    }

    fun clearContextualActionMode() {
        if (this::mActionMode.isInitialized) {
            mActionMode.finish()
        }
    }
}