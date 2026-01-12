package com.mustafa.influencer.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mustafa.influencer.data.repository.UserRepositoryImpl
import com.mustafa.influencer.domain.model.UserType
import com.mustafa.influencer.domain.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AuthUiState(
    val currentStep: Int = 1,
    val selectedUserType: UserType? = null,
    val email: String = "",
    val password: String = "",
    val isLogin: Boolean = true,
    val isLoading: Boolean = false,
    val errorMessage: String = ""
)

class AuthViewModel(
    private val repo: UserRepository = UserRepositoryImpl()
) : ViewModel() {

    private val _state = MutableStateFlow(AuthUiState())
    val state: StateFlow<AuthUiState> = _state.asStateFlow()

    fun selectUserType(type: UserType) {
        _state.value = _state.value.copy(
            selectedUserType = type,
            currentStep = 2,
            errorMessage = ""
        )
    }

    fun setEmail(value: String) {
        _state.value = _state.value.copy(email = value, errorMessage = "")
    }

    fun setPassword(value: String) {
        _state.value = _state.value.copy(password = value, errorMessage = "")
    }

    fun toggleLogin() {
        _state.value = _state.value.copy(isLogin = !_state.value.isLogin, errorMessage = "")
    }

    fun goBack() {
        _state.value = _state.value.copy(
            currentStep = 1,
            email = "",
            password = "",
            errorMessage = ""
        )
    }

    fun submit(onNextRoute: (String) -> Unit) {
        val s = _state.value
        val type = s.selectedUserType

        if (type == null) {
            _state.value = s.copy(errorMessage = "Kullanıcı tipi seçilmedi")
            return
        }
        if (s.email.isBlank() || s.password.isBlank()) {
            _state.value = s.copy(errorMessage = "Lütfen tüm alanları doldurun")
            return
        }

        viewModelScope.launch {
            _state.value = s.copy(isLoading = true, errorMessage = "")

            // 1) Auth
            val authRes: Result<String> = runCatching {
                if (s.isLogin) repo.signIn(s.email, s.password)
                else repo.signUp(s.email, s.password, type)
            }

            authRes.onFailure { e ->
                _state.value = _state.value.copy(
                    isLoading = false,
                    errorMessage = mapAuthError(e)
                )
                return@launch
            }

            // 2) Route
            val nextRoute: String = runCatching {
                val uid = repo.currentUserId() ?: return@runCatching Screen.Auth.route
                val user = repo.getUser(uid)

                when {
                    user.userType == "influencer" && !user.profileCompleted -> Screen.ProfileSetup.route
                    user.userType == "influencer" -> Screen.Influencer.route
                    user.userType == "advertiser" && !user.profileCompleted -> Screen.AdvertiserProfileSetup.route
                    user.userType == "advertiser" -> Screen.Advertiser.route
                    else -> Screen.Auth.route
                }
            }.getOrElse { Screen.Auth.route }

            _state.value = _state.value.copy(isLoading = false)
            onNextRoute(nextRoute)
        }
    }

    private fun mapAuthError(e: Throwable): String {
        val msg = e.message.orEmpty()
        return when {
            msg.contains("password", true) -> "Şifre en az 6 karakter olmalıdır"
            msg.contains("email", true) -> "Geçersiz e-posta adresi"
            msg.contains("user-not-found", true) -> "Kullanıcı bulunamadı"
            msg.contains("wrong-password", true) -> "Yanlış şifre"
            msg.contains("email-already-in-use", true) -> "Bu e-posta zaten kullanımda"
            else -> if (msg.isNotBlank()) msg else "Bir hata oluştu"
        }
    }
}
