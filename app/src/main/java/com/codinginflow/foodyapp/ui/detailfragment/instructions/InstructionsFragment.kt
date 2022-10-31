package com.codinginflow.foodyapp.ui.detailfragment.instructions

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import com.codinginflow.foodyapp.R
import com.codinginflow.foodyapp.databinding.FragmentIngredientsBinding
import com.codinginflow.foodyapp.databinding.FragmentInstructionsBinding
import com.codinginflow.foodyapp.databinding.FragmentOverviewBinding
import com.codinginflow.foodyapp.model.ResultRecipe
import com.codinginflow.foodyapp.util.Constants.Companion.RECIPE_RESULT_KEY

class InstructionsFragment : Fragment() {

    private var _binding: FragmentInstructionsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInstructionsBinding.inflate(inflater, container, false)

        val args = arguments
        val myBundle: ResultRecipe? = args?.getParcelable(RECIPE_RESULT_KEY)

        binding.instructionWebView.webViewClient = object : WebViewClient() {}
        val websiteUrl : String? = myBundle!!.sourceUrl
        if (websiteUrl != null) {
            binding.instructionWebView.loadUrl(websiteUrl)
        }

        return binding.root
    }
}