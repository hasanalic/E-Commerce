package com.hasanalic.ecommerce.feature_home.domain.use_case.home_use_cases

import com.hasanalic.ecommerce.core.domain.model.DataError
import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.feature_home.domain.repository.HomeRepository
import javax.inject.Inject

class GetProductEntityIdByBarcodeUseCase @Inject constructor(
    private val homeRepository: HomeRepository
) {
    suspend operator fun invoke(productBarcode: String): Result<Int, DataError.Local> {
        return homeRepository.getProductEntityIdByBarcode(productBarcode)
    }
}