package com.codinginflow.foodyapp.ui.detailfragment.ingredients

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.codinginflow.foodyapp.R
import com.codinginflow.foodyapp.adapter.IngredientsAdapter
import com.codinginflow.foodyapp.adapter.RecipesAdapter
import com.codinginflow.foodyapp.databinding.FragmentIngredientsBinding
import com.codinginflow.foodyapp.databinding.FragmentOverviewBinding
import com.codinginflow.foodyapp.model.ResultRecipe
import com.codinginflow.foodyapp.util.Constants.Companion.RECIPE_RESULT_KEY

class IngredientsFragment : Fragment() {

    private var _binding: FragmentIngredientsBinding? = null
    private val binding get() = _binding!!
    private val mAdapter by lazy { IngredientsAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentIngredientsBinding.inflate(inflater, container, false)

        val args = arguments
        val myBundle: ResultRecipe? = args?.getParcelable(RECIPE_RESULT_KEY)

        binding.ingredientsRecyclerView.adapter = mAdapter
        binding.ingredientsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        myBundle?.let { mAdapter.setData(it) }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}