package com.hasanalic.ecommerce.feature_auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hasanalic.ecommerce.core.domain.DataError
import com.hasanalic.ecommerce.core.domain.PasswordError
import com.hasanalic.ecommerce.core.domain.Result
import com.hasanalic.ecommerce.feature_auth.domain.use_cases.AuthUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authUseCases: AuthUseCases
): ViewModel() {

    fun onRegisterClick(name: String, email: String, password: String) {
        when(val result = authUseCases.userPasswordValidatorUseCase(password)) {
            is Result.Error -> {
                when(result.error) {
                    PasswordError.TOO_SHORT -> TODO()
                    PasswordError.NO_UPPERCASE -> TODO()
                    PasswordError.NO_DIGIT -> TODO()
                }
            }
            is Result.Success -> {
                register(name, email, password)
            }
        }
    }

    private fun register(name: String, email: String, password: String) {
        viewModelScope.launch {
            when(val result = authUseCases.insertUserUseCase(name, email, password)) {
                is Result.Error -> registerErrorHandle(result.error)
                is Result.Success -> {
                    // notify ui
                }
            }
        }
    }

    private fun registerErrorHandle(dataError: DataError) {
        when(dataError) {
            DataError.Local.DISK_FULL -> {}
            DataError.Local.DB_CORRUPTION -> {}
            DataError.Local.QUERY_FAILED -> {}
            DataError.Local.INSERTION_FAILD -> println("SHOW ERROR MESSAGE")
            DataError.Local.UPDATE_FAILED -> {}
            DataError.Local.DELETION_FAILED -> {}
            DataError.Local.CONSTRAINT_VIOLATION -> {}
            DataError.Local.DATA_CONVERSION -> {}
            DataError.Local.MIGRATION_FAILED -> {}
            DataError.Local.ACCESS_DENIED -> {}
            DataError.Local.UNKNOWN -> {}
            DataError.Network.REQUEST_TIMEOUT -> {}
            DataError.Network.TOO_MANY_REQUEST -> {}
            DataError.Network.NO_INTERNET -> {}
            DataError.Network.PAYLOAD_TOO_LARGE -> {}
            DataError.Network.SERVER_ERROR -> {}
            DataError.Network.SERIALIZATION -> {}
            DataError.Network.UNKNOWN -> {}
        }
    }
}