package com.hasanalic.ecommerce.core.domain

enum class PasswordError: Error {
    TOO_SHORT,
    NO_UPPERCASE,
    NO_DIGIT
}