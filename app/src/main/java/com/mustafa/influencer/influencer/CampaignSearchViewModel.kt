package com.mustafa.influencer.influencer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mustafa.influencer.data.repository.CampaignRepositoryImpl
import com.mustafa.influencer.domain.model.Campaign
import com.mustafa.influencer.domain.repository.CampaignRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class CampaignSearchUiState(
    val isLoading: Boolean = false,
    val errorMessage: String = "",
    val all: List<Campaign> = emptyList(),
    val searchQuery: String = "",
    val selectedCategory: String = "Tümü"
)

class CampaignSearchViewModel(
    private val repo: CampaignRepository = CampaignRepositoryImpl()
) : ViewModel() {

    private val _state = MutableStateFlow(CampaignSearchUiState())
    val state: StateFlow<CampaignSearchUiState> = _state.asStateFlow()

    fun setQuery(v: String) {
        _state.value = _state.value.copy(searchQuery = v)
    }

    fun setCategory(v: String) {
        _state.value = _state.value.copy(selectedCategory = v)
    }

    fun refresh() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, errorMessage = "")
            runCatching { repo.listActiveCampaigns() }
                .onSuccess { list ->
                    _state.value = _state.value.copy(isLoading = false, all = list)
                }
                .onFailure { e ->
                    _state.value = _state.value.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Kampanyalar alınamadı"
                    )
                }
        }
    }

    fun filtered(): List<Campaign> {
        val s = _state.value
        val q = s.searchQuery.trim()

        return s.all.filter { c ->
            val matchCategory = (s.selectedCategory == "Tümü" || c.category == s.selectedCategory)
            val matchQuery = (q.isEmpty()
                    || c.title.contains(q, ignoreCase = true)
                    || c.advertiserName.contains(q, ignoreCase = true))
            matchCategory && matchQuery
        }
    }
}
