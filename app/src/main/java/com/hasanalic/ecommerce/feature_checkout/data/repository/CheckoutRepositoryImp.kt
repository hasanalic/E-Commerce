package com.hasanalic.ecommerce.feature_checkout.data.repository

import com.hasanalic.ecommerce.feature_orders.data.local.entity.OrderEntity
import com.hasanalic.ecommerce.feature_orders.data.local.entity.OrderProductsEntity
import com.hasanalic.ecommerce.feature_checkout.data.local.entity.CardEntity
import com.hasanalic.ecommerce.feature_orders.data.local.OrderDao
import com.hasanalic.ecommerce.feature_orders.data.local.OrderProductsDao
import com.hasanalic.ecommerce.feature_checkout.data.local.CardDao
import com.hasanalic.ecommerce.feature_home.data.local.ShoppingCartItemsDao
import com.hasanalic.ecommerce.feature_checkout.domain.repository.CheckoutRepository
import com.hasanalic.ecommerce.utils.Resource

/*
class CheckoutRepositoryImp (
    private val orderDao: OrderDao,
    private val orderProductsDao: OrderProductsDao,
    private val cardDao: CardDao,
    private val shoppingCartItemsDao: ShoppingCartItemsDao
) : CheckoutRepository {

    override suspend fun insertOrder(orderEntity: OrderEntity): Resource<Long> {
        return try {
            val response = orderDao.insertOrder(orderEntity)
            response?.let {
                Resource.Success(it)
            }?: Resource.Error(null,"Sipariş kaydedilemedi.")
        } catch (e: Exception) {
            Resource.Error(null,e.localizedMessage?:"Bilinmeyen bir hata meydana geldi")
        }
    }

    override suspend fun insertAllOrderProducts(vararg orderProductsEntity: OrderProductsEntity): Resource<Boolean> {
        return try {
            val response = orderProductsDao.insertAllOrderProducts(*orderProductsEntity)
            if (response.isNotEmpty()) {
                Resource.Success(true)
            } else {
                Resource.Error(false,"Sipariş ürünleri kaydedilemedi.")
            }
        } catch (e: Exception) {
            Resource.Error(null,e.localizedMessage?:"Bilinmeyen bir hata meydana geldi")
        }
    }

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