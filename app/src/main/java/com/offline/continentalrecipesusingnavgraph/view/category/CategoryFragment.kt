package com.offline.continentalrecipesusingnavgraph.view.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import com.offline.continentalrecipesusingnavgraph.adapter.RecyclerViewAdapter
import com.offline.continentalrecipesusingnavgraph.databinding.FragmentCategoryBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoryFragment : Fragment() {

    private lateinit var binding: FragmentCategoryBinding
    private lateinit var adapter: RecyclerViewAdapter
    private val viewModel: CategoryViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCategoryBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = RecyclerViewAdapter{
            setFragmentResult("selectedCategory", bundleOf("category" to it))
        }
        binding.categoryRecyclerView.adapter = adapter
        viewModel.category.observe(viewLifecycleOwner) {
            adapter.submitList(it.categories)
        }
    }
}