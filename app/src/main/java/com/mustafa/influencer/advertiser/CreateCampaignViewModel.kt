package com.mustafa.influencer.advertiser

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mustafa.influencer.data.repository.CampaignRepositoryImpl
import com.mustafa.influencer.domain.repository.CampaignRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class CreateCampaignUiState(
    val title: String = "",
    val description: String = "",
    val budgetText: String = "",
    val platform: String = "Instagram",
    val category: String = "Teknoloji",
    val deadlineText: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String = "",
    val successMessage: String = ""
)

class CreateCampaignViewModel(
    private val repo: CampaignRepository = CampaignRepositoryImpl()
) : ViewModel() {

    private val _state = MutableStateFlow(CreateCampaignUiState())
    val state: StateFlow<CreateCampaignUiState> = _state.asStateFlow()

    fun setTitle(v: String) = update { it.copy(title = v, errorMessage = "", successMessage = "") }
    fun setDescription(v: String) = update { it.copy(description = v, errorMessage = "", successMessage = "") }
    fun setBudgetText(v: String) = update { it.copy(budgetText = v, errorMessage = "", successMessage = "") }
    fun setPlatform(v: String) = update { it.copy(platform = v, errorMessage = "", successMessage = "") }
    fun setCategory(v: String) = update { it.copy(category = v, errorMessage = "", successMessage = "") }
    fun setDeadlineText(v: String) = update { it.copy(deadlineText = v, errorMessage = "", successMessage = "") }

    fun create(status: String, onDone: () -> Unit) {
        val s = _state.value

        val budget = s.budgetText.trim().toLongOrNull()
        when {
            s.title.isBlank() -> update { it.copy(errorMessage = "Başlık boş olamaz") }
            s.description.isBlank() -> update { it.copy(errorMessage = "Açıklama boş olamaz") }
            budget == null || budget <= 0 -> update { it.copy(errorMessage = "Bütçe sayı olmalı (örn: 25000)") }
            s.deadlineText.isBlank() -> update { it.copy(errorMessage = "Son başvuru alanı boş olamaz") }
            else -> {
                viewModelScope.launch {
                    update { it.copy(isLoading = true, errorMessage = "", successMessage = "") }

                    runCatching {
                        repo.createCampaign(
                            title = s.title,
                            description = s.description,
                            platform = s.platform,
                            category = s.category,
                            budget = budget,
                            deadlineText = s.deadlineText,
                            status = status
                        )
                    }.onSuccess {
                        update { it.copy(isLoading = false, successMessage = "Kampanya kaydedildi ✅") }
                        onDone()
                    }.onFailure { e ->
                        update { it.copy(isLoading = false, errorMessage = e.message ?: "Kayıt başarısız") }
                    }
                }
            }
        }
    }

    private inline fun update(block: (CreateCampaignUiState) -> CreateCampaignUiState) {
        _state.value = block(_state.value)
    }
}
