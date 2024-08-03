package com.hasanalic.ecommerce.feature_checkout.data.repository

import com.hasanalic.ecommerce.feature_location.data.entity.AddressEntity
import com.hasanalic.ecommerce.feature_orders.data.entity.OrderEntity
import com.hasanalic.ecommerce.feature_orders.data.entity.OrderProductsEntity
import com.hasanalic.ecommerce.feature_checkout.data.entity.PaymentEntity
import com.hasanalic.ecommerce.feature_location.data.local.AddressDao
import com.hasanalic.ecommerce.feature_orders.data.local.OrderDao
import com.hasanalic.ecommerce.feature_orders.data.local.OrderProductsDao
import com.hasanalic.ecommerce.feature_checkout.data.local.PaymentDao
import com.hasanalic.ecommerce.feature_shopping_cart.data.local.ShoppingCartItemsDao
import com.hasanalic.ecommerce.feature_location.data.mapper.toAddress
import com.hasanalic.ecommerce.core.domain.model.Address
import com.hasanalic.ecommerce.feature_checkout.domain.repository.CheckoutRepository
import com.hasanalic.ecommerce.utils.Resource

class CheckoutRepositoryImp (
    private val addressDao: AddressDao,
    private val orderDao: OrderDao,
    private val orderProductsDao: OrderProductsDao,
    private val paymentDao: PaymentDao,
    private val shoppingCartItemsDao: ShoppingCartItemsDao
) : CheckoutRepository {
    override suspend fun getAddressesByUserId(userId: String): Resource<List<Address>> {
        return try {
            val response = addressDao.getAddressesByUserId(userId)
            response?.let {
                return@let Resource.Success(it.map { addressEntity ->  addressEntity.toAddress() })
            }?: Resource.Error(null,"Address bilgisi bulunamadı.")
        } catch (e: Exception) {
            Resource.Error(null,e.localizedMessage?:"Bilinmeyen bir hata meydana geldi")
        }
    }

    override suspend fun insertAddress(addressEntity: AddressEntity): Resource<Boolean> {
        return try {
            val response = addressDao.insertAddress(addressEntity)
            if (response > 0) {
                Resource.Success(true)
            } else {
                Resource.Error(false,"Adres kaydedilemedi.")
            }
        } catch (e: Exception) {
            Resource.Error(null,e.localizedMessage?:"Bilinmeyen bir hata meydana geldi")
        }
    }

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

    override suspend fun insertCard(paymentEntity: PaymentEntity): Resource<Long> {
        return try {
            val response = paymentDao.insertCard(paymentEntity)
            response?.let {
                Resource.Success(it)
            }?: Resource.Error(null,"Kart kaydedilemedi.")
        } catch (e: Exception) {
            Resource.Error(null,e.localizedMessage?:"Bilinmeyen bir hata meydana geldi")
        }
    }

    override suspend fun getCardsByUserId(userId: String): Resource<List<PaymentEntity>> {
        return try {
            val response = paymentDao.getCardsByUserId(userId)
            response?.let {
                Resource.Success(it)
            }?: Resource.Error(null,"Kart bilgisi alınamadı.")
        } catch (e: Exception) {
            Resource.Error(null,e.localizedMessage?:"Bilinmeyen bir hata meydana geldi")
        }
    }

    override suspend fun deleteShoppingCartItemsByProductIds(userId: String, productIds: List<String>): Resource<Boolean> {
        return try {
            val response = shoppingCartItemsDao.deleteShoppingCartItemsByProductIdList(userId, productIds)
            if (response > 0) {
                Resource.Success(true)
            } else {
                Resource.Error(false,"Alışveriş sepeti silinemedi.")
            }
        } catch (e: Exception) {
            Resource.Error(null,e.localizedMessage?:"Bilinmeyen bir hata meydana geldi")
        }
    }

    override suspend fun deleteAddress(userId: String, addressId: String): Resource<Boolean> {
        return try {
            val response = addressDao.deleteAddress(userId, addressId)
            if (response > 0) {
                Resource.Success(true)
            } else {
                Resource.Error(false,"Adres silinemedi.")
            }
        } catch (e: Exception) {
            Resource.Error(null,e.localizedMessage?:"Bilinmeyen bir hata meydana geldi.")
        }
    }
}