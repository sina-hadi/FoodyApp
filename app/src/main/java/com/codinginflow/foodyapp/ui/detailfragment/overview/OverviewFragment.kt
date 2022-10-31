package com.codinginflow.foodyapp.ui.detailfragment.overview

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import coil.load
import com.codinginflow.foodyapp.R
import com.codinginflow.foodyapp.databinding.FragmentOverviewBinding
import com.codinginflow.foodyapp.model.ResultRecipe
import com.codinginflow.foodyapp.util.Constants.Companion.RECIPE_RESULT_KEY

class OverviewFragment : Fragment() {

    private var _binding: FragmentOverviewBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOverviewBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this

        val args = arguments
        val myBundle: ResultRecipe? = args?.getParcelable(RECIPE_RESULT_KEY)
        binding.bundle = myBundle

        return binding.root
    }
}