package com.hasanalic.ecommerce.feature_location.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Address")
data class AddressEntity(
    @ColumnInfo(name = "address_user_id") var addressUserId: String,
    @ColumnInfo(name = "address_title") var addressTitle: String,
    @ColumnInfo(name = "address_detail") var addressDetail: String
) {
    @PrimaryKey(autoGenerate = true)
    var addressId: Int = 0
}