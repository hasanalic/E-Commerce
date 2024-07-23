package com.hasanalic.ecommerce.domain.repository

import com.hasanalic.ecommerce.data.dto.AddressEntity
import com.hasanalic.ecommerce.data.dto.OrderEntity
import com.hasanalic.ecommerce.data.dto.OrderProductsEntity
import com.hasanalic.ecommerce.data.dto.PaymentEntity
import com.hasanalic.ecommerce.domain.model.Address
import com.hasanalic.ecommerce.utils.Resource

interface CheckoutRepository {
    suspend fun getAddressesByUserId(userId: String): Resource<List<Address>>

    suspend fun insertAddress(addressEntity: AddressEntity): Resource<Boolean>

    suspend fun insertOrder(orderEntity: OrderEntity): Resource<Long>

    suspend fun insertAllOrderProducts(vararg orderProductsEntity: OrderProductsEntity): Resource<Boolean>

    suspend fun insertCard(paymentEntity: PaymentEntity): Resource<Long>

    suspend fun getCardsByUserId(userId: String): Resource<List<PaymentEntity>>

    suspend fun deleteShoppingCartItemsByProductIds(userId: String, productIds: List<String>): Resource<Boolean>

    suspend fun deleteAddress(userId: String, addressId: String): Resource<Boolean>
}