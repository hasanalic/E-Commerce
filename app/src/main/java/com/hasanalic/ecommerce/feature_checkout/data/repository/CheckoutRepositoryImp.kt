package com.hasanalic.ecommerce.feature_checkout.data.repository

/*
class CheckoutRepositoryImp (
    private val orderDao: OrderDao,
    private val orderProductsDao: OrderProductsDao,
    private val cardDao: CardDao,
    private val shoppingCartItemsDao: ShoppingCartItemsDao
) : CheckoutRepository {

    override suspend fun deleteShoppingCartItemsByProductIds(userId: String, productIds: List<String>): Resource<Boolean> {
        return try {
            val response = shoppingCartItemsDao.deleteShoppingCartItemEntitiesByProductIdList(userId, productIds)
            if (response > 0) {
                Resource.Success(true)
            } else {
                Resource.Error(false,"Alışveriş sepeti silinemedi.")
            }
        } catch (e: Exception) {
            Resource.Error(null,e.localizedMessage?:"Bilinmeyen bir hata meydana geldi")
        }
    }
}

 */