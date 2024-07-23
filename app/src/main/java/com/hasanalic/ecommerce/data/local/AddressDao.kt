package com.hasanalic.ecommerce.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.hasanalic.ecommerce.data.dto.AddressEntity

@Dao
interface AddressDao {

    @Query("SELECT * FROM Address WHERE address_user_id = :userId")
    suspend fun getAddressesByUserId(userId: String): List<AddressEntity>?

    @Insert
    suspend fun insertAddress(addressEntity: AddressEntity): Long

    @Query("DELETE FROM Address WHERE address_user_id = :userId AND addressId = :addressId")
    suspend fun deleteAddress(userId: String, addressId: String): Int

    @Query("SELECT * FROM Address WHERE address_user_id = :userId AND addressId = :addressId")
    suspend fun getAddressByUserIdAndAddressId(userId: String, addressId: String): AddressEntity?
}