package com.codinginflow.foodyapp.ui.mainfragments

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.navArgs
import com.codinginflow.foodyapp.R
import com.codinginflow.foodyapp.adapter.PagerAdapter
import com.codinginflow.foodyapp.data.database.entities.FavoritesEntity
import com.codinginflow.foodyapp.databinding.ActivityDetailsBinding
import com.codinginflow.foodyapp.ui.detailfragment.ingredients.IngredientsFragment
import com.codinginflow.foodyapp.ui.detailfragment.instructions.InstructionsFragment
import com.codinginflow.foodyapp.ui.detailfragment.overview.OverviewFragment
import com.codinginflow.foodyapp.util.Constants.Companion.RECIPE_RESULT_KEY
import com.codinginflow.foodyapp.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsActivity : AppCompatActivity() {

    private val args by navArgs<DetailsActivityArgs>()
    private val mainViewModel: MainViewModel by viewModels()
    private var isFavoriteRecipe: Boolean = false

    private lateinit var binding: ActivityDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        setSupportActionBar(binding.toolbar)
        binding.toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val fragments = ArrayList<Fragment>()
        fragments.add(OverviewFragment())
        fragments.add(IngredientsFragment())
        fragments.add(InstructionsFragment())

        val titles = ArrayList<String>()
        titles.add("Overview")
        titles.add("Ingredients")
        titles.add("Instructions")

        val resultBundle = Bundle()
        resultBundle.putParcelable(RECIPE_RESULT_KEY, args.result)

        val pageAdapter = PagerAdapter(
            resultBundle,
            fragments,
            this
        )

        binding.viewPager.apply {
            adapter = pageAdapter
        }
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = titles[position]
        }.attach()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.details_menu, menu)
        val menuItem = menu?.findItem(R.id.save_to_favorite_menu)
        menuItem?.icon?.setTint(ContextCompat.getColor(this, R.color.white))
        checkSavedRecipe(menuItem!!)
        return true
    }

    private fun checkSavedRecipe(menuItem: MenuItem): Boolean {
        mainViewModel.readFavoriteRecipes.observe(this) {
            try {
                for (savedRecipe in it) {
                    if (savedRecipe.result.id == args.result.id) {
                        menuItem.icon?.setTint(ContextCompat.getColor(this, R.color.yellow))
                        isFavoriteRecipe = true
                        break
                    }
                }
            } catch (e: Exception) {
                Log.e("ABCD", e.message.toString())
            }
        }
        return isFavoriteRecipe
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        } else if (item.itemId == R.id.save_to_favorite_menu) {
            isFavoriteRecipe = if (isFavoriteRecipe) {
                deleteFromFavorite()
                item.icon?.setTint(ContextCompat.getColor(this, R.color.white))
                false
            } else {
                saveToFavorite()
                item.icon?.setTint(ContextCompat.getColor(this, R.color.yellow))
                true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteFromFavorite() {
        mainViewModel.deleteFavoriteRecipe(args.result.id)
        Snackbar.make(binding.detailsLayout, "Recipe deleted.", Snackbar.LENGTH_SHORT)
            .setAction("Okay") {}.show()
    }

    private fun saveToFavorite() {
        val favoritesEntity = FavoritesEntity(args.result.id, args.result)
        mainViewModel.insertFavoriteRecipe(favoritesEntity)
        Snackbar.make(binding.detailsLayout, "Recipe saved.", Snackbar.LENGTH_SHORT)
            .setAction("Okay") {}.show()
    }
}