package com.hasanalic.ecommerce.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class UserPrefs

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DbPrefs