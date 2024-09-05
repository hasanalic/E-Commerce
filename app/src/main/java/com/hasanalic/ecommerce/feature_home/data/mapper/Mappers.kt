package com.hasanalic.ecommerce.feature_home.data.mapper

import com.hasanalic.ecommerce.feature_auth.data.local.entities.UserEntity
import com.hasanalic.ecommerce.feature_home.domain.model.FavoriteProduct
import com.hasanalic.ecommerce.feature_home.data.local.entity.FavoriteProductDto
import com.hasanalic.ecommerce.feature_home.data.local.entity.ProductDto
import com.hasanalic.ecommerce.feature_home.data.local.entity.ShoppingCartItemDto
import com.hasanalic.ecommerce.feature_home.domain.model.Product
import com.hasanalic.ecommerce.feature_home.domain.model.ShoppingCartItem
import com.hasanalic.ecommerce.feature_home.domain.model.User

fun ProductDto.toProduct() =
    Product(
        productId = this.productId.toString(),
        productCategory = this.productCategory,
        productPhoto = this.productPhoto,
        productBrand = this.productBrand,
        productDetail = this.productDetail,
        productPriceWhole = this.productPriceWhole,
        productPriceCent = this.productPriceCent,
        productRate = this.productRate,
        productReviewCount = this.productReviewCount,
        productBarcode = this.productBarcode,
        addedToShoppingCart = this.inCart,
        addedToFavorites = this.inFavorite
    )

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

fun UserEntity.toUser(): User {
    return User(userId = this.userId.toString(), userName = this.userName, userEmail = this.userEmail)
}