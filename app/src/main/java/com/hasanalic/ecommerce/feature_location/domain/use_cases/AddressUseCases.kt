package com.hasanalic.ecommerce.feature_location.domain.use_cases

data class AddressUseCases(
    val deleteUserAddressUseCase: DeleteUserAddressUseCase,
    val getAddressEntityByUserIdAndAddressIdUseCase: GetAddressEntityByUserIdAndAddressIdUseCase,
    val getLocationListByUserIdUseCase: GetLocationListByUserIdUseCase,
    val getAddressListByUserIdUseCase: GetAddressListByUserIdUseCase,
    val insertAddressEntityUseCase: InsertAddressEntityUseCase,
    val addressValidatorUseCase: AddressValidatorUseCase
)