package com.codinginflow.foodyapp.ui.mainfragments.favorite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.codinginflow.foodyapp.R
import com.codinginflow.foodyapp.adapter.FavoriteAdapter
import com.codinginflow.foodyapp.adapter.RecipesAdapter
import com.codinginflow.foodyapp.databinding.FragmentFavoriteRecipesBinding
import com.codinginflow.foodyapp.databinding.FragmentRecipesBinding
import com.codinginflow.foodyapp.util.observeOnce
import com.codinginflow.foodyapp.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavoriteRecipesFragment : Fragment() {

    private var _binding: FragmentFavoriteRecipesBinding? = null
    private val binding get() = _binding!!

    private lateinit var mainViewModel: MainViewModel
    private val mAdapter by lazy { FavoriteAdapter(requireActivity(), mainViewModel) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteRecipesBinding.inflate(inflater, container, false)
        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]

        setupRecyclerView()
        readDatabase()

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        mAdapter.clearContextualActionMode()
    }

    private fun setupRecyclerView() {
        binding.favoriteRecyclerView.adapter = mAdapter
        binding.favoriteRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun readDatabase() {
        mainViewModel.readFavoriteRecipes.observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                binding.emptyLinearLayout.visibility = View.VISIBLE
            } else {
                binding.emptyLinearLayout.visibility = View.INVISIBLE
                mAdapter.setData(it)
            }
        }
    }
}