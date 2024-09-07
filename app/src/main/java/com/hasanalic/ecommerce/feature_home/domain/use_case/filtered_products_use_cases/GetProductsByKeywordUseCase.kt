package com.hasanalic.ecommerce.feature_home.domain.use_case.filtered_products_use_cases

import com.hasanalic.ecommerce.core.domain.model.DataError
import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.feature_home.domain.model.Product
import com.hasanalic.ecommerce.feature_home.domain.repository.FilteredProductsRepository
import javax.inject.Inject

class GetProductsByKeywordUseCase @Inject constructor(
    private val filteredProductsRepository: FilteredProductsRepository
) {
    suspend operator fun invoke(userId: String, keyword: String): Result<List<Product>, DataError.Local> {
        return filteredProductsRepository.getProductsByKeyword(userId, keyword)
    }
}