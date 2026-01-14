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

data class CampaignDetailUiState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val campaign: Campaign? = null
)

class CampaignDetailViewModel(
    private val repo: CampaignRepository = CampaignRepositoryImpl()
) : ViewModel() {

    private val _state = MutableStateFlow(CampaignDetailUiState())
    val state: StateFlow<CampaignDetailUiState> = _state.asStateFlow()

    fun loadCampaign(id: String) {
        viewModelScope.launch {
            _state.value = CampaignDetailUiState(isLoading = true)
            runCatching {
                repo.getCampaign(id)
            }.onSuccess { campaign ->
                _state.value = CampaignDetailUiState(isLoading = false, campaign = campaign)
            }.onFailure { e ->
                _state.value = CampaignDetailUiState(isLoading = false, error = e.message)
            }
        }
    }
}