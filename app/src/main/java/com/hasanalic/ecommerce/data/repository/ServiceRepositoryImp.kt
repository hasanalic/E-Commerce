package com.hasanalic.ecommerce.data.repository

import com.hasanalic.ecommerce.data.dto.NotificationEntity
import com.hasanalic.ecommerce.data.local.NotificationDao
import com.hasanalic.ecommerce.data.local.ShoppingCartItemsDao
import com.hasanalic.ecommerce.domain.repository.ServiceRepository
import com.hasanalic.ecommerce.utils.Resource
import javax.inject.Inject

class ServiceRepositoryImp @Inject constructor (
    private val shoppingCartDao: ShoppingCartItemsDao,
    private val notificationDao: NotificationDao
) : ServiceRepository {
    override fun getShoppingCartCount(userId: String): Int {
        return shoppingCartDao.getCount(userId)
    }

    override suspend fun getNotifications(): Resource<List<NotificationEntity>> {
        return try {
            val response = notificationDao.getNotifications()
            response?.let {
                Resource.Success(it)
            }?: Resource.Error(null,"Bildirim bilgisi bulunamadı.")
        } catch (e: Exception) {
            Resource.Error(null,e.localizedMessage?:"Bilinmeyen bir hata meydana geldi.")
        }
    }

    override suspend fun insertNotification(notificationEntity: NotificationEntity): Resource<Boolean> {
        return try {
            val response = notificationDao.insertNotification(notificationEntity)
            if (response > 0) {
                Resource.Success(true)
            } else {
                Resource.Error(null,"Bildirim kaydedilemedi.")
            }
        } catch (e: Exception) {
            Resource.Error(null,e.localizedMessage?:"Bilinmeyen bir hata meydana geldi.")
        }
    }
}