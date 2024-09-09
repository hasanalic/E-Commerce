package com.hasanalic.ecommerce.feature_filter.presentation

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.hasanalic.ecommerce.R
import com.hasanalic.ecommerce.databinding.ActivityFilterBinding
import com.hasanalic.ecommerce.feature_filter.presentation.util.Filter
import com.hasanalic.ecommerce.feature_filter.presentation.util.FilterSingleton
import com.hasanalic.ecommerce.feature_filter.presentation.views.BrandAdapter
import com.hasanalic.ecommerce.feature_filter.presentation.views.CategoryAdapter
import com.hasanalic.ecommerce.core.presentation.utils.ItemDecoration
import com.hasanalic.ecommerce.core.utils.hide
import com.hasanalic.ecommerce.core.utils.show
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FilterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFilterBinding

    private lateinit var viewModel: FilterViewModel

    private val categoryAdapter by lazy {
        CategoryAdapter()
    }

    private val brandAdapter by lazy {
        BrandAdapter()
    }

    private lateinit var filter: Filter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFilterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        filter = Filter()

        viewModel = ViewModelProvider(this)[FilterViewModel::class.java]
        viewModel.getCategoryAndBrandList()

        setupListeners()

        setupRecyclerViewCategory()

        setupRecyclerViewBrand()

        setupObservers()
    }

    private fun setupListeners() {
        val intent = Intent()

        binding.buttonApply.setOnClickListener {
            val minPrice = binding.textInputEditTextMinPrice.text.toString()
            val maxPrice = binding.textInputEditTextMaxPrice.text.toString()
            if (minPrice != "") filter.minPrice = minPrice.toInt()
            if (maxPrice != "") filter.maxPrice = maxPrice.toInt()

            FilterSingleton.filter = filter

            intent.putExtra(getString(R.string.should_move_to_filtered_fragment), true)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }

        binding.buttonReset.setOnClickListener {
            reset()
        }

        binding.toolBarFilter.setNavigationOnClickListener {
            intent.putExtra(getString(R.string.should_move_to_filtered_fragment), false)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                intent.putExtra(getString(R.string.should_move_to_filtered_fragment), false)
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        })

        setRadioButtons()
    }

    private fun setupRecyclerViewCategory() {
        binding.recyclerViewCategory.adapter = categoryAdapter
        binding.recyclerViewCategory.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerViewCategory.addItemDecoration(ItemDecoration(0,16,0))

        categoryAdapter.setOnCategoryClickListener { category, position ->
            filter.category = category
            filter.brand = null
            viewModel.selectCategory(position, category)
            categoryAdapter.notifyChanges()
        }
    }

    private fun setupRecyclerViewBrand() {
        binding.recyclerViewBrand.adapter = brandAdapter
        binding.recyclerViewBrand.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false)
        binding.recyclerViewBrand.addItemDecoration(ItemDecoration(0,16,0))

        brandAdapter.setOnBrandClickListener { brand, position ->
            if (brand == "All") {
                filter.brand = null
            } else {
                filter.brand = brand
            }

            viewModel.selectBrand(position)
            brandAdapter.notifyChanges()
        }
    }

    private fun setupObservers() {
        viewModel.filterState.observe(this) { filterState ->
            handleFilterState(filterState)
        }
    }

    private fun handleFilterState(state: FilterState) {
        if (state.isLoading) {
            binding.progressBarFilter.show()
            binding.buttonReset.isEnabled = false
            binding.buttonApply.isEnabled = false
        } else {
            binding.progressBarFilter.hide()
            binding.buttonReset.isEnabled = true
            binding.buttonApply.isEnabled = true
        }

        state.categoryList.let {
            categoryAdapter.categoryList = it
            categoryAdapter.notifyChanges()
        }

        state.brandList.let {
            brandAdapter.brandList = it
            brandAdapter.notifyChanges()
        }

        state.dataError?.let {
            binding.recyclerViewBrand.hide()
            binding.recyclerViewCategory.hide()
            TODO("DATA ERROR TEXTVIEW")
        }

        state.actionError?.let {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }
    }

    private fun setRadioButtons() {
        binding.radioButtonFourFiveAndAbove.setOnClickListener {
            filter.minStar = 4.5
            filter.maxStar = 5.0
            binding.radioButtonFourFiveAndAbove.isChecked = true
            binding.radioButtonFourAndFourFive.isChecked = false
            binding.radioButtonThreeFiveAndFour.isChecked = false
            binding.radioButtonThreeAndThreeFive.isChecked = false
            binding.radioButtonTwoFiveAndThree.isChecked = false
            binding.radioButtonTwoAndTwoFive.isChecked = false
        }
        binding.radioButtonFourAndFourFive.setOnClickListener {
            filter.minStar = 4.0
            filter.maxStar = 4.5
            binding.radioButtonFourFiveAndAbove.isChecked = false
            binding.radioButtonFourAndFourFive.isChecked = true
            binding.radioButtonThreeFiveAndFour.isChecked = false
            binding.radioButtonThreeAndThreeFive.isChecked = false
            binding.radioButtonTwoFiveAndThree.isChecked = false
            binding.radioButtonTwoAndTwoFive.isChecked = false
        }
        binding.radioButtonThreeFiveAndFour.setOnClickListener {
            filter.minStar = 3.5
            filter.maxStar = 4.0
            binding.radioButtonFourFiveAndAbove.isChecked = false
            binding.radioButtonFourAndFourFive.isChecked = false
            binding.radioButtonThreeFiveAndFour.isChecked = true
            binding.radioButtonThreeAndThreeFive.isChecked = false
            binding.radioButtonTwoFiveAndThree.isChecked = false
            binding.radioButtonTwoAndTwoFive.isChecked = false
        }
        binding.radioButtonThreeAndThreeFive.setOnClickListener {
            filter.minStar = 3.0
            filter.maxStar = 3.5
            binding.radioButtonFourFiveAndAbove.isChecked = false
            binding.radioButtonFourAndFourFive.isChecked = false
            binding.radioButtonThreeFiveAndFour.isChecked = false
            binding.radioButtonThreeAndThreeFive.isChecked = true
            binding.radioButtonTwoFiveAndThree.isChecked = false
            binding.radioButtonTwoAndTwoFive.isChecked = false
        }
        binding.radioButtonTwoFiveAndThree.setOnClickListener {
            filter.minStar = 2.5
            filter.maxStar = 3.0
            binding.radioButtonFourFiveAndAbove.isChecked = false
            binding.radioButtonFourAndFourFive.isChecked = false
            binding.radioButtonThreeFiveAndFour.isChecked = false
            binding.radioButtonThreeAndThreeFive.isChecked = false
            binding.radioButtonTwoFiveAndThree.isChecked = true
            binding.radioButtonTwoAndTwoFive.isChecked = false
        }
        binding.radioButtonTwoAndTwoFive.setOnClickListener {
            filter.minStar = 2.0
            filter.maxStar = 2.5
            binding.radioButtonFourFiveAndAbove.isChecked = false
            binding.radioButtonFourAndFourFive.isChecked = false
            binding.radioButtonThreeFiveAndFour.isChecked = false
            binding.radioButtonThreeAndThreeFive.isChecked = false
            binding.radioButtonTwoFiveAndThree.isChecked = false
            binding.radioButtonTwoAndTwoFive.isChecked = true
        }
    }

    private fun reset() {
        filter = Filter()
        viewModel.getCategoryAndBrandList()
        categoryAdapter.notifyChanges()
        brandAdapter.notifyChanges()
        binding.recyclerViewBrand.scrollToPosition(0)
        binding.recyclerViewCategory.scrollToPosition(0)

        binding.textInputEditTextMinPrice.setText("")
        binding.textInputEditTextMaxPrice.setText("")
        binding.radioButtonFourFiveAndAbove.isChecked = false
        binding.radioButtonFourAndFourFive.isChecked = false
        binding.radioButtonThreeFiveAndFour.isChecked = false
        binding.radioButtonThreeAndThreeFive.isChecked = false
        binding.radioButtonTwoFiveAndThree.isChecked = false
        binding.radioButtonTwoAndTwoFive.isChecked = false
    }
}