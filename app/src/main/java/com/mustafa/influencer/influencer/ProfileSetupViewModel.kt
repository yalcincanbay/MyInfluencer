package com.mustafa.influencer.influencer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mustafa.influencer.data.repository.UserRepositoryImpl
import com.mustafa.influencer.domain.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ProfileSetupUiState(
    val selectedPlatforms: Set<String> = emptySet(),
    val youtubeLink: String = "",
    val tiktokLink: String = "",
    val instagramLink: String = "",
    val selectedCategories: Set<String> = emptySet(),
    val bio: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String = ""
)

class ProfileSetupViewModel(
    private val repo: UserRepository = UserRepositoryImpl()
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileSetupUiState())
    val state: StateFlow<ProfileSetupUiState> = _state.asStateFlow()

    fun togglePlatform(platformLower: String) {
        val s = _state.value
        _state.value = s.copy(
            selectedPlatforms = if (s.selectedPlatforms.contains(platformLower)) s.selectedPlatforms - platformLower
            else s.selectedPlatforms + platformLower,
            errorMessage = ""
        )
    }

    fun toggleCategory(categoryLower: String) {
        val s = _state.value
        _state.value = s.copy(
            selectedCategories = if (s.selectedCategories.contains(categoryLower)) s.selectedCategories - categoryLower
            else s.selectedCategories + categoryLower,
            errorMessage = ""
        )
    }

    fun setYoutube(v: String) { _state.value = _state.value.copy(youtubeLink = v, errorMessage = "") }
    fun setTiktok(v: String) { _state.value = _state.value.copy(tiktokLink = v, errorMessage = "") }
    fun setInstagram(v: String) { _state.value = _state.value.copy(instagramLink = v, errorMessage = "") }
    fun setBio(v: String) { _state.value = _state.value.copy(bio = v, errorMessage = "") }

    fun submit(withLinks: Boolean, onSuccess: () -> Unit) {
        val s = _state.value

        if (s.selectedPlatforms.isEmpty()) {
            _state.value = s.copy(errorMessage = "Lütfen en az bir platform seçin")
            return
        }
        if (s.selectedCategories.isEmpty()) {
            _state.value = s.copy(errorMessage = "Lütfen en az bir kategori seçin")
            return
        }

        val platformLinks = if (withLinks) {
            buildMap {
                if (s.youtubeLink.isNotBlank()) put("youtube", s.youtubeLink)
                if (s.tiktokLink.isNotBlank()) put("tiktok", s.tiktokLink)
                if (s.instagramLink.isNotBlank()) put("instagram", s.instagramLink)
            }
        } else emptyMap()

        val bio = if (withLinks) s.bio else ""

        viewModelScope.launch {
            _state.value = s.copy(isLoading = true, errorMessage = "")

            runCatching {
                val uid = repo.currentUserId() ?: error("Oturum bulunamadı")

                repo.saveInfluencerProfile(
                    userId = uid,
                    platforms = s.selectedPlatforms.toList(),
                    platformLinks = platformLinks,
                    categories = s.selectedCategories.toList(),
                    bio = bio
                )
            }.onSuccess {
                _state.value = _state.value.copy(isLoading = false)
                onSuccess()
            }.onFailure { e ->
                _state.value = _state.value.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Profil kaydedilemedi"
                )
            }
        }
    }
}
