package com.hasanalic.ecommerce.feature_filter.domain.use_cases

import com.hasanalic.ecommerce.core.domain.model.DataError
import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.feature_filter.domain.repository.FilterRepository
import com.hasanalic.ecommerce.feature_home.domain.model.Brand
import javax.inject.Inject

class GetBrandsUseCase @Inject constructor(
    private val filterRepository: FilterRepository
) {
    suspend operator fun invoke(): Result<List<Brand>, DataError.Local> {
        return filterRepository.getBrands()
    }
}