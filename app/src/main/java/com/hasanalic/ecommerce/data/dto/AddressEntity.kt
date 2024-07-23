package com.hasanalic.ecommerce.data.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Address")
data class AddressEntity(
    @ColumnInfo(name = "address_user_id") var addressUserId: String? = null,
    @ColumnInfo(name = "address_title") var addressTitle: String? = null,
    @ColumnInfo(name = "address_detail") var addressDetail: String? = null,
) {
    @PrimaryKey(autoGenerate = true)
    var addressId: Int = 0
}