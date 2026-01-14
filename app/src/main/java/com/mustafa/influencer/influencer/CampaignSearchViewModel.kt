package com.mustafa.influencer.influencer

import android.util.Log
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
                    Log.d("ViewModelDebug", "ViewModel listeyi aldı. Boyut: ${list.size}")
                    _state.value = _state.value.copy(isLoading = false, all = list)
                }
                .onFailure { e ->
                    Log.e("ViewModelDebug", "Hata oluştu", e)
                    _state.value = _state.value.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Kampanyalar alınamadı"
                    )
                }
        }
    }

    // Filtreleme mantığını UI State üzerinden yapacak şekilde güncelledik
    fun filtered(): List<Campaign> {
        val s = _state.value
        // Eğer liste boşsa direkt boş dön
        if (s.all.isEmpty()) return emptyList()

        val q = s.searchQuery.trim()

        return s.all.filter { c ->
            // Kategori kontrolü (Büyük/küçük harf duyarsız yapalım garanti olsun)
            val matchCategory = (s.selectedCategory == "Tümü" ||
                    c.category.equals(s.selectedCategory, ignoreCase = true))

            // Arama kontrolü
            val matchQuery = (q.isEmpty()
                    || c.title.contains(q, ignoreCase = true)
                    || c.advertiserName.contains(q, ignoreCase = true))

            matchCategory && matchQuery
        }
    }
}