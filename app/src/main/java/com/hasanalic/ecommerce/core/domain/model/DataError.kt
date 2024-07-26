package com.hasanalic.ecommerce.core.domain.model

sealed interface DataError: Error {
    enum class Network: DataError {
        REQUEST_TIMEOUT,
        TOO_MANY_REQUEST,
        NO_INTERNET,
        PAYLOAD_TOO_LARGE,
        SERVER_ERROR,
        SERIALIZATION,
        UNKNOWN
    }

    enum class Local: DataError {
        QUERY_FAILED,
        INSERTION_FAILD,
        UPDATE_FAILED,
        DELETION_FAILED,
        UNKNOWN
        //DISK_FULL,
        //DB_CORRUPTION,
        //CONSTRAINT_VIOLATION,
        //DATA_CONVERSION,
        //MIGRATION_FAILED,
        //ACCESS_DENIED,
    }
}