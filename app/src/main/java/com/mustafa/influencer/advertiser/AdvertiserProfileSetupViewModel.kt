package com.mustafa.influencer.advertiser

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mustafa.influencer.data.repository.UserRepositoryImpl
import com.mustafa.influencer.domain.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AdvertiserProfileSetupUiState(
    val companyName: String = "",
    val selectedPlatforms: Set<String> = emptySet(),
    val selectedCategories: Set<String> = emptySet(),
    val isLoading: Boolean = false,
    val errorMessage: String = ""
)

class AdvertiserProfileSetupViewModel(
    private val repo: UserRepository = UserRepositoryImpl()
) : ViewModel() {

    private val _state = MutableStateFlow(AdvertiserProfileSetupUiState())
    val state: StateFlow<AdvertiserProfileSetupUiState> = _state.asStateFlow()

    fun setCompanyName(v: String) { _state.value = _state.value.copy(companyName = v, errorMessage = "") }

    fun togglePlatform(platformLower: String) {
        val s = _state.value
        _state.value = s.copy(
            selectedPlatforms = if (s.selectedPlatforms.contains(platformLower)) {
                s.selectedPlatforms - platformLower
            } else s.selectedPlatforms + platformLower,
            errorMessage = ""
        )
    }

    fun toggleCategory(categoryLower: String) {
        val s = _state.value
        _state.value = s.copy(
            selectedCategories = if (s.selectedCategories.contains(categoryLower)) {
                s.selectedCategories - categoryLower
            } else s.selectedCategories + categoryLower,
            errorMessage = ""
        )
    }

    fun submit(onSuccess: () -> Unit) {
        val s = _state.value

        when {
            s.companyName.isBlank() -> { _state.value = s.copy(errorMessage = "Lütfen şirket ismini girin"); return }
            s.selectedPlatforms.isEmpty() -> { _state.value = s.copy(errorMessage = "Lütfen en az bir platform seçin"); return }
            s.selectedCategories.isEmpty() -> { _state.value = s.copy(errorMessage = "Lütfen en az bir alan seçin"); return }
        }

        viewModelScope.launch {
            _state.value = s.copy(isLoading = true, errorMessage = "")

            runCatching {
                val uid = repo.currentUserId() ?: error("Oturum bulunamadı")
                repo.saveAdvertiserProfile(
                    userId = uid,
                    companyName = s.companyName,
                    platforms = s.selectedPlatforms.toList(),
                    categories = s.selectedCategories.toList()
                )
            }.onSuccess {
                _state.value = _state.value.copy(isLoading = false)
                onSuccess()
            }.onFailure { e ->
                _state.value = _state.value.copy(isLoading = false, errorMessage = e.message ?: "Profil kaydedilemedi")
            }
        }
    }

}
