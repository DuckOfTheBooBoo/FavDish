package com.arajdianaltaf.favdish.view.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.arajdianaltaf.favdish.R
import com.arajdianaltaf.favdish.application.FavDishApplication
import com.arajdianaltaf.favdish.databinding.FragmentAllDishesBinding
import com.arajdianaltaf.favdish.view.activities.AddUpdateDishActivity
import com.arajdianaltaf.favdish.view.adapters.FavDishAdapter
import com.arajdianaltaf.favdish.viewmodel.FavDishViewModel
import com.arajdianaltaf.favdish.viewmodel.FavDishViewModelFactory
import com.arajdianaltaf.favdish.viewmodel.HomeViewModel

class AllDishesFragment : Fragment() {

    private lateinit var myBinding: FragmentAllDishesBinding
    private val myFavDishViewModel: FavDishViewModel by viewModels {
        FavDishViewModelFactory(
            (
                    requireActivity().application as FavDishApplication
                    ).repository)
    }
    private var _binding: FragmentAllDishesBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        myBinding = FragmentAllDishesBinding.inflate(inflater, container, false)
        return myBinding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        myBinding.rvDishesList.layoutManager = GridLayoutManager(requireActivity(), 2)
        val favDishAdapter = FavDishAdapter(this@AllDishesFragment)

        myBinding.rvDishesList.adapter = favDishAdapter

        myFavDishViewModel.allDishesList.observe(viewLifecycleOwner) {
            dishes -> dishes.let {
                if (it.isNotEmpty()) {

                    myBinding.rvDishesList.visibility = View.VISIBLE
                    myBinding.tvNoDishesAddedYet.visibility = View.GONE

                    favDishAdapter.dishesList(it)
                } else {

                    myBinding.rvDishesList.visibility = View.GONE
                    myBinding.tvNoDishesAddedYet.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_all_dishes, menu)

        super.onCreateOptionsMenu(menu, inflater)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_add_dish -> {
                startActivity(Intent(requireActivity(), AddUpdateDishActivity::class.java))
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }
}