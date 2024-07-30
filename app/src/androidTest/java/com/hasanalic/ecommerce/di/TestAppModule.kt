package com.hasanalic.ecommerce.di

import android.content.Context
import androidx.room.Room
import com.hasanalic.ecommerce.core.data.local.MyDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TestAppModule {

    @Provides
    @Named("test_db")
    @Singleton
    fun provideInMemoryDb(@ApplicationContext context: Context) =
        Room.inMemoryDatabaseBuilder(context, MyDatabase::class.java)
        .allowMainThreadQueries()
        .build()
}