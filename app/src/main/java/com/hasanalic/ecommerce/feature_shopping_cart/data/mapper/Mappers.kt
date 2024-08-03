package com.hasanalic.ecommerce.feature_shopping_cart.data.mapper

import com.hasanalic.ecommerce.feature_shopping_cart.data.entity.ShoppingCartItemDto
import com.hasanalic.ecommerce.feature_shopping_cart.domain.model.ShoppingCartItem

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