package com.hasanalic.ecommerce.feature_favorite.data.mapper

import com.hasanalic.ecommerce.feature_favorite.data.entity.FavoriteProductDto
import com.hasanalic.ecommerce.feature_favorite.domain.model.FavoriteProduct
import com.hasanalic.ecommerce.feature_home.data.entity.ProductEntity
import com.hasanalic.ecommerce.feature_home.domain.model.Product

fun FavoriteProductDto.toFavoriteProduct() =
    FavoriteProduct(
        productId = this.productId.toString(),
        category = this.productCategory,
        photo = this.productPhoto,
        brand = this.productBrand,
        detail = this.productDetail,
        priceWhole = this.productPriceWhole,
        priceCent = this.productPriceCent,
        rate = this.productRate,
        reviewCount = this.productReviewCount,
        addedToShoppingCart = inCart
    )