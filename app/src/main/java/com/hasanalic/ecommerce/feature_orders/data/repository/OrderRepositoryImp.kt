package com.hasanalic.ecommerce.feature_orders.data.repository

import com.hasanalic.ecommerce.core.domain.model.DataError
import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.feature_orders.data.local.entity.OrderEntity
import com.hasanalic.ecommerce.feature_orders.data.local.OrderDao
import com.hasanalic.ecommerce.feature_orders.data.mapper.toOrder
import com.hasanalic.ecommerce.feature_orders.data.mapper.toOrderDetail
import com.hasanalic.ecommerce.feature_orders.domain.model.Order
import com.hasanalic.ecommerce.feature_orders.domain.model.OrderDetail
import com.hasanalic.ecommerce.feature_orders.domain.repository.OrderRepository
import javax.inject.Inject

class OrderRepositoryImp @Inject constructor(
    private val orderDao: OrderDao
): OrderRepository {
    override suspend fun insertOrder(order: OrderEntity): Result<Unit, DataError.Local> {
        return try {
            val result = orderDao.insertOrder(order)
            if (result > 0) {
                Result.Success(Unit)
            } else {
                Result.Error(DataError.Local.INSERTION_FAILED)
            }
        } catch (e: Exception) {
            Result.Error(DataError.Local.UNKNOWN)
        }
    }

    override suspend fun getOrderDetail(orderId: String): Result<OrderDetail, DataError.Local> {
        return try {
            val result = orderDao.getOrderDetail(orderId)
            result?.let {
                Result.Success(result.toOrderDetail())
            }?: Result.Error(DataError.Local.NOT_FOUND)
        } catch (e: Exception) {
            Result.Error(DataError.Local.UNKNOWN)
        }
    }

    override suspend fun getOrdersByUser(userId: String): Result<List<Order>, DataError.Local> {
        return try {
            val result = orderDao.getOrdersWithProductsByUser(userId)
            result?.let {
                Result.Success(result.map { it.toOrder() })
            }?: Result.Error(DataError.Local.NOT_FOUND)
        } catch (e: Exception) {
            Result.Error(DataError.Local.UNKNOWN)
        }
    }

    override suspend fun updateOrderStatus(newStatus: String, userId: String, orderId: String): Result<Unit, DataError.Local> {
        return try {
            val result = orderDao.updateOrderStatus(newStatus, userId, orderId)
            if (result > 0) {
                Result.Success(Unit)
            } else {
                Result.Error(DataError.Local.UPDATE_FAILED)
            }
        } catch (e: Exception) {
            Result.Error(DataError.Local.UNKNOWN)
        }
    }


    /*
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
        TODO()
    }

     */

    /*
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

     */

    /*
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

     */

    /*
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

     */

    /*
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

     */

    /*
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

     */
}