package com.hasanalic.ecommerce.feature_orders.data.repository

import com.hasanalic.ecommerce.feature_location.data.entity.AddressEntity
import com.hasanalic.ecommerce.feature_orders.data.entity.OrderEntity
import com.hasanalic.ecommerce.feature_orders.data.entity.OrderProductsEntity
import com.hasanalic.ecommerce.feature_checkout.data.local.entity.PaymentEntity
import com.hasanalic.ecommerce.feature_location.data.local.AddressDao
import com.hasanalic.ecommerce.feature_orders.data.local.OrderDao
import com.hasanalic.ecommerce.feature_orders.data.local.OrderProductsDao
import com.hasanalic.ecommerce.feature_checkout.data.local.PaymentDao
import com.hasanalic.ecommerce.feature_orders.domain.repository.OrderRepository
import com.hasanalic.ecommerce.utils.Resource
import javax.inject.Inject

class OrderRepositoryImp @Inject constructor(
    private val addressDao: AddressDao,
    private val orderDao: OrderDao,
    private val orderProductsDao: OrderProductsDao,
    private val paymentDao: PaymentDao
): OrderRepository {
    override suspend fun getAddressByUserIdAndAddressId(
        userId: String,
        addressId: String
    ): Resource<AddressEntity> {
        return try {
            val response = addressDao.getAddressByUserIdAndAddressId(userId, addressId)
            response?.let {
                Resource.Success(it)
            }?: Resource.Error(null,"Adres bilgisi bulunamadı.")
        } catch (e: Exception) {
            Resource.Error(null,e.localizedMessage?:"Bilinmeyen bir hata meydana geldi.")
        }
    }

    override suspend fun getOrdersByUserId(userId: String): Resource<List<OrderEntity>> {
        return try {
            val response = orderDao.getOrdersByUserId(userId)
            response?.let {
                Resource.Success(it)
            }?: Resource.Error(null,"Sipariş bilgisi alınamadı.")
        } catch (e: Exception) {
            Resource.Error(null,e.localizedMessage?:"Bilinmeyen bir hata meydana geldi.")
        }
    }

    override suspend fun getOrder(userId: String, orderId: String): Resource<OrderEntity> {
        return try {
            val response = orderDao.getOrder(userId, orderId)
            response?.let {
                Resource.Success(it)
            }?: Resource.Error(null,"Sipariş bilgisi alınamadı.")
        } catch (e: Exception) {
            Resource.Error(null,e.localizedMessage?:"Bilinmeyen bir hata meydana geldi.")
        }
    }

    override suspend fun updateOrderStatus(
        newStatus: String,
        userId: String,
        orderId: String
    ): Resource<Boolean> {
        return try {
            val response = orderDao.updateOrderStatus(newStatus, userId, orderId)
            if (response > 0) {
                Resource.Success(true)
            } else {
                Resource.Error(null,"Sipariş durumu güncellenemedi.")
            }
        } catch (e: Exception) {
            Resource.Error(null,e.localizedMessage?:"Bilinmeyen bir hata meydana geldi.")
        }
    }

    override suspend fun getOrderProductsList(
        userId: String,
        orderId: String
    ): Resource<List<OrderProductsEntity>> {
        return try {
            val response = orderProductsDao.getOrderProductsList(userId, orderId)
            response?.let {
                Resource.Success(it)
            }?: Resource.Error(null,"Ürün bilgisi bulunamadı.")
        } catch (e: Exception) {
            Resource.Error(null,e.localizedMessage?:"Bilinmeyen bir hata meydana geldi.")
        }
    }

    override suspend fun getCardByUserId(
        userId: String,
        paymentId: String
    ): Resource<PaymentEntity> {
        return try {
            val response = paymentDao.getCardByUserId(userId, paymentId)
            response?.let {
                Resource.Success(it)
            }?: Resource.Error(null,"Ödeme bilgisi bulunamadı.")
        } catch (e: Exception) {
            Resource.Error(null,e.localizedMessage?:"Bilinmeyen bir hata meydana geldi.")
        }
    }
}