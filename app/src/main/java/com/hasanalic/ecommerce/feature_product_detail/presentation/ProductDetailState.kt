package com.hasanalic.ecommerce.feature_product_detail.presentation

import com.hasanalic.ecommerce.feature_product_detail.data.local.entity.ReviewEntity
import com.hasanalic.ecommerce.feature_product_detail.domain.model.ProductDetail

data class ProductDetailState(
    var isLoading: Boolean = false,
    var productDetail: ProductDetail? = null,
    var reviewList: List<ReviewEntity> = emptyList(),
    var dataError: String? = null,
    var actionError: String? = null
)