package com.hasanalic.ecommerce.feature_auth.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.filters.SmallTest
import androidx.test.runner.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.hasanalic.ecommerce.core.data.local.MyDatabase
import com.hasanalic.ecommerce.feature_auth.data.local.entities.UserEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@SmallTest
@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class UserDaoTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var userDao: UserDao
    private lateinit var database: MyDatabase

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            MyDatabase::class.java
        ).allowMainThreadQueries().build()

        userDao = database.userDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    private suspend fun insertTestUserEntity(): UserEntity {
        val userEntity = UserEntity("username", "username@example.com", "Password123")
        userDao.insertUser(userEntity)
        return userEntity
    }

    @Test
    fun getUserByEmailAndPassword_success() = runBlocking {
        val expectedUserEntity = insertTestUserEntity()

        val fetchedUserEntity = userDao.getUserByEmailAndPassword(expectedUserEntity.userEmail, expectedUserEntity.userPassword)

        assertThat(fetchedUserEntity).isEqualTo(expectedUserEntity)
    }

    @Test
    fun getUserByEmailAndPassword_userNotFound() = runBlocking {
        val email = "username@example.com"
        val password = "Password123"

        val result = userDao.getUserByEmailAndPassword(email, password)

        assertThat(result).isNull()
    }

    @Test
    fun getUserByEmailAndPassword_wrongPassword() = runBlocking{
        val expectedUserEntity = insertTestUserEntity()

        val wrongPassword = "Password123123"

        val result = userDao.getUserByEmailAndPassword(expectedUserEntity.userEmail, wrongPassword)
        assertThat(result).isNull()
    }

    @Test
    fun getUserByEmailAndPassword_wrongEmail() = runBlocking {
        val expectedUserEntity = insertTestUserEntity()

        val wrongEmail = "wrong@example.com"

        val result = userDao.getUserByEmailAndPassword(wrongEmail, expectedUserEntity.userPassword)
        assertThat(result).isNull()
    }

    @Test
    fun getUserByEmailAndPassword_wrongEmailAndPassword() = runBlocking {
        insertTestUserEntity()

        val wrongEmail = "wrong@example.com"
        val wrongPassword = "Password123123"

        val result = userDao.getUserByEmailAndPassword(wrongEmail, wrongPassword)
        assertThat(result).isNull()
    }

}