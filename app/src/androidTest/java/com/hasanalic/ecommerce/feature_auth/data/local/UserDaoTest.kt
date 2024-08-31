package com.hasanalic.ecommerce.feature_auth.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import com.hasanalic.ecommerce.core.data.local.MyDatabase
import com.hasanalic.ecommerce.feature_auth.data.local.entities.UserEntity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Named

@SmallTest
@ExperimentalCoroutinesApi
@HiltAndroidTest
class UserDaoTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Inject
    @Named("test_db")
    lateinit var database: MyDatabase
    private lateinit var userDao: UserDao

    @Before
    fun setup() {
        hiltRule.inject()

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
    fun getUserIdByEmailAndPassword_success() = runBlocking {
        val userEntity = UserEntity("username", "username@example.com", "Password123")
        val insertedUserId = userDao.insertUser(userEntity)

        val fetchedUserId = userDao.getUserIdByEmailAndPassword("username@example.com", "Password123")

        assertThat(fetchedUserId).isEqualTo(insertedUserId.toInt())
    }

    @Test
    fun getUserIdByEmailAndPassword_userNotFound() = runBlocking {
        val email = "username@example.com"
        val password = "Password123"

        val result = userDao.getUserIdByEmailAndPassword(email, password)

        assertThat(result).isNull()
    }

    @Test
    fun getUserIdByEmailAndPassword_wrongPassword() = runBlocking{
        val expectedUserEntity = insertTestUserEntity()

        val wrongPassword = "Password123123"

        val result = userDao.getUserIdByEmailAndPassword(expectedUserEntity.userEmail, wrongPassword)
        assertThat(result).isNull()
    }

    @Test
    fun getUserIdByEmailAndPassword_wrongEmail() = runBlocking {
        val expectedUserEntity = insertTestUserEntity()

        val wrongEmail = "wrong@example.com"

        val result = userDao.getUserIdByEmailAndPassword(wrongEmail, expectedUserEntity.userPassword)
        assertThat(result).isNull()
    }

    @Test
    fun getUserIdByEmailAndPassword_wrongEmailAndPassword() = runBlocking {
        insertTestUserEntity()

        val wrongEmail = "wrong@example.com"
        val wrongPassword = "Password123123"

        val result = userDao.getUserIdByEmailAndPassword(wrongEmail, wrongPassword)
        assertThat(result).isNull()
    }

}