package com.hasanalic.ecommerce.feature_home.domain.use_case.home_use_cases

import com.hasanalic.ecommerce.core.domain.model.DataError
import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.feature_home.domain.model.Category
import com.hasanalic.ecommerce.feature_home.domain.repository.HomeRepository
import javax.inject.Inject

class GetCategoriesUseCase @Inject constructor(
    private val homeRepository: HomeRepository
) {
    suspend operator fun invoke(): Result<List<Category>, DataError.Local> {
        return homeRepository.getCategories()
    }
}