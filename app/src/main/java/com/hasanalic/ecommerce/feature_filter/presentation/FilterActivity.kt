package com.hasanalic.ecommerce.feature_filter.presentation

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.hasanalic.ecommerce.R
import com.hasanalic.ecommerce.databinding.ActivityFilterBinding
import com.hasanalic.ecommerce.feature_filter.presentation.util.Filter
import com.hasanalic.ecommerce.feature_filter.presentation.util.FilterSingleton
import com.hasanalic.ecommerce.feature_home.presentation.filtered_screen.FilterAdapter
import com.hasanalic.ecommerce.utils.ItemDecoration
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FilterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFilterBinding

    private lateinit var viewModel: FilterViewModel

    private val categoryFilterAdapter by lazy {
        FilterAdapter()
    }

    private val brandFilterAdapter by lazy {
        FilterAdapter()
    }

    private lateinit var filter: Filter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFilterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = Intent()
        filter = Filter()

        viewModel = ViewModelProvider(this)[FilterViewModel::class.java]
        viewModel.getCategoryAndBrandList()

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

        setRecyclerView()

        setRadioButtons()

        observe()
    }

    private fun observe() {
        /*
        viewModel.stateCategoryList.observe(this) {
            categoryFilterAdapter.chipList = it
            categoryFilterAdapter.notifyChanges()
        }

        viewModel.stateBrandList.observe(this) {
            brandFilterAdapter.chipList = it
            brandFilterAdapter.notifyChanges()
        }

         */
    }

    private fun setRecyclerView() {
        binding.recyclerViewCategory.adapter = categoryFilterAdapter
        binding.recyclerViewCategory.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false)
        binding.recyclerViewCategory.addItemDecoration(ItemDecoration(0,16,0))
        categoryFilterAdapter.setOnChipClickListener {
            filter.category = it
            //viewModel.selectCategory(it)
            filter.brand = null
            categoryFilterAdapter.notifyChanges()
        }

        binding.recyclerViewBrand.adapter = brandFilterAdapter
        binding.recyclerViewBrand.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false)
        binding.recyclerViewBrand.addItemDecoration(ItemDecoration(0,16,0))
        brandFilterAdapter.setOnChipClickListener {
            if (it == "Hepsi") {
                filter.brand = null
            } else {
                filter.brand = it
            }
            //viewModel.selectBrand(it)
            brandFilterAdapter.notifyChanges()
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
        categoryFilterAdapter.notifyChanges()
        brandFilterAdapter.notifyChanges()
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