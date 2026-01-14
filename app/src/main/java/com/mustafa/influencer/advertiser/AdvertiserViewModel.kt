package com.mustafa.influencer.advertiser

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mustafa.influencer.data.repository.CampaignRepositoryImpl
import com.mustafa.influencer.domain.repository.CampaignRepository
import com.mustafa.influencer.shared.FirebaseManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AdvertiserUiState(
    val isLoading: Boolean = false,
    val myCampaigns: List<com.mustafa.influencer.domain.model.Campaign> = emptyList(),
    val totalBudget: String = "0"
)

class AdvertiserViewModel(
    private val repo: CampaignRepository = CampaignRepositoryImpl()
) : ViewModel() {

    private val _state = MutableStateFlow(AdvertiserUiState())
    val state: StateFlow<AdvertiserUiState> = _state.asStateFlow()

    init {
        loadMyCampaigns()
    }

    fun loadMyCampaigns() {
        val uid = FirebaseManager.getCurrentUserId()
        if (uid == null) {
            Log.e("AdvertiserDebug", "Kullanıcı ID (UID) bulunamadı! Giriş yapılmamış olabilir.")
            return
        }

        Log.d("AdvertiserDebug", "Veri çekiliyor... Kullanıcı ID: $uid")

        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            runCatching {
                repo.getCampaignsByAdvertiser(uid)
            }.onSuccess { list ->
                Log.d("AdvertiserDebug", "Başarılı! ${list.size} kampanya geldi.")
                val total = list.sumOf { it.budget }
                _state.value = _state.value.copy(
                    isLoading = false,
                    myCampaigns = list,
                    totalBudget = "₺$total"
                )
            }.onFailure { e ->
                // LİNKI GÖRMEK İÇİN BURAYA BAKMALISIN
                Log.e("AdvertiserDebug", "HATA OLUŞTU! Muhtemelen İndeks Eksik.", e)
                _state.value = _state.value.copy(isLoading = false)
            }
        }
    }
}