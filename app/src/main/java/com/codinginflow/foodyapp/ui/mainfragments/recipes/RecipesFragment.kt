package com.codinginflow.foodyapp.ui.mainfragments.recipes

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.codinginflow.foodyapp.viewmodel.MainViewModel
import com.codinginflow.foodyapp.R
import com.codinginflow.foodyapp.adapter.RecipesAdapter
import com.codinginflow.foodyapp.databinding.FragmentRecipesBinding
import com.codinginflow.foodyapp.util.NetworkListener
import com.codinginflow.foodyapp.util.NetworkResult
import com.codinginflow.foodyapp.util.observeOnce
import com.codinginflow.foodyapp.viewmodel.RecipesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
@AndroidEntryPoint
class RecipesFragment : Fragment(), SearchView.OnQueryTextListener {

    private val args by navArgs<RecipesFragmentArgs>()

    private lateinit var mainViewModel: MainViewModel
    private lateinit var recipesViewModel: RecipesViewModel
    private val mAdapter by lazy { RecipesAdapter() }
    private var _binding: FragmentRecipesBinding? = null
    private val binding get() = _binding!!

    private lateinit var networkListener: NetworkListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        recipesViewModel = ViewModelProvider(requireActivity())[RecipesViewModel::class.java]
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipesBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.mainViewModel = mainViewModel


        setupRecyclerView()
        readDatabase()

        lifecycleScope.launch {
            networkListener = NetworkListener()
            networkListener.checkNetworkAvailability(requireContext())
                .collect {
                    Log.e("ABCD", it.toString())
                    recipesViewModel.networkStatus = it
                }
        }

        binding.floatingActionButton.setOnClickListener {
            if (recipesViewModel.networkStatus) {
                findNavController().navigate(R.id.action_recipesFragment_to_recipesBottomSheet)
            } else {
                Toast.makeText(requireContext(),"No Internet Connection",Toast.LENGTH_SHORT).show()
            }
        }


        return binding.root
    }

    private fun setupRecyclerView() {
        binding.recipesRecyclerView.adapter = mAdapter
        binding.recipesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun readDatabase() {
        lifecycleScope.launch {
            mainViewModel.readRecipes.observeOnce(viewLifecycleOwner) { database ->
                if (database.isNotEmpty() && !args.backFromBottomSheet) {
                    mAdapter.setData(database[0].foodRecipe)
                } else {
                    requestApiData()
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost = requireHost() as MenuHost
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.recipes_menu, menu)

                val search = menu.findItem(R.id.menu_search)
                val searchView = search.actionView as? SearchView
                searchView?.isSubmitButtonEnabled = true
                searchView?.setOnQueryTextListener(this@RecipesFragment)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun requestApiData() {
        lifecycleScope.launch {
            mainViewModel.getRecipes(recipesViewModel.applyQueries())
            mainViewModel.recipesResponse.observe(viewLifecycleOwner) { response ->
                if (response is NetworkResult.Success) {
                    response.data?.let { mAdapter.setData(it) }
                }
            }
        }
    }

    private fun searchApiData(searchQuery: String) {
        mainViewModel.searchRecipes(recipesViewModel.applySearchQuery(searchQuery))
        mainViewModel.searchedRecipesResponse.observe(viewLifecycleOwner) { response ->
            if (response is NetworkResult.Success) {
                response.data?.let { mAdapter.setData(it) }
            }
        }
    }


    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query != null) {
            searchApiData(query)
        }
        return true
    }


    override fun onQueryTextChange(newText: String?): Boolean {
        return true
    }
}