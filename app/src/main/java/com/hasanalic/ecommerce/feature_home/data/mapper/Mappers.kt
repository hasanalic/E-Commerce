package com.hasanalic.ecommerce.feature_home.data.mapper

import com.hasanalic.ecommerce.feature_home.domain.model.FavoriteProduct
import com.hasanalic.ecommerce.feature_home.data.local.entity.FavoriteProductDto
import com.hasanalic.ecommerce.feature_home.data.local.entity.ShoppingCartItemDto
import com.hasanalic.ecommerce.feature_home.domain.model.ShoppingCartItem

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

fun ShoppingCartItemDto.toShoppingCartItem() =
    ShoppingCartItem(
        productId = this.productId.toString(),
        category = this.productCategory,
        photo = this.productPhoto,
        brand = this.productBrand,
        detail = this.productDetail,
        priceWhole = this.productPriceWhole,
        priceCent = this.productPriceCent,
        quantity = this.quantity
    )