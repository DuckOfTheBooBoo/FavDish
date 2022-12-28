package com.arajdianaltaf.favdish.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.arajdianaltaf.favdish.application.FavDishApplication
import com.arajdianaltaf.favdish.databinding.FragmentFavoriteDishesBinding
import com.arajdianaltaf.favdish.view.adapters.FavDishAdapter
import com.arajdianaltaf.favdish.viewmodel.DashboardViewModel
import com.arajdianaltaf.favdish.viewmodel.FavDishViewModel
import com.arajdianaltaf.favdish.viewmodel.FavDishViewModelFactory

class FavoriteDishesFragment : Fragment() {

    private lateinit var myBinding: FragmentFavoriteDishesBinding
    private val myFavDishViewModel: FavDishViewModel by viewModels {
        FavDishViewModelFactory(
            (
                    requireActivity().application as FavDishApplication).repository)

    }


    private var _binding: FragmentFavoriteDishesBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        myBinding = FragmentFavoriteDishesBinding.inflate(inflater, container, false)

        return myBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        myBinding.rvFavDishesList.layoutManager = GridLayoutManager(requireActivity(), 2)

        val favDishAdapter = FavDishAdapter(this@FavoriteDishesFragment)

        myBinding.rvFavDishesList.adapter = favDishAdapter

        myFavDishViewModel.allFavDishesList.observe(viewLifecycleOwner) {
            dishes -> dishes.let {
                if (it.isNotEmpty()) {

                    myBinding.rvFavDishesList.visibility = View.VISIBLE
                    myBinding.tvNoFavDishesAddedYet.visibility = View.GONE
                } else {

                    myBinding.rvFavDishesList.visibility = View.GONE
                    myBinding.tvNoFavDishesAddedYet.visibility = View.VISIBLE
                }
        }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}