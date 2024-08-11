package com.hasanalic.ecommerce.feature_product_detail.data.mapper

import com.hasanalic.ecommerce.feature_home.data.local.entity.ProductDto
import com.hasanalic.ecommerce.feature_product_detail.domain.model.ProductDetail

fun ProductDto.toProductDetail() =
    ProductDetail(
        productId = this.productId.toString(),
        productCategory = this.productCategory,
        productPhoto = this.productPhoto,
        productBrand = this.productBrand,
        productDetail = this.productDetail,
        productPriceWhole = this.productPriceWhole,
        productPriceCent = this.productPriceCent,
        productRate = this.productRate,
        productReviewCount = this.productReviewCount,
        productShipping = this.productShipping,
        productStore = this.productStore,
        productStoreRate = this.productStoreRate,
        addedToShoppingCart = this.inCart,
        addedToFavorites = this.inFavorite
    )