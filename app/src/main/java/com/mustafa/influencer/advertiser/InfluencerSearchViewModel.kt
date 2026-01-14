package com.mustafa.influencer.advertiser

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mustafa.influencer.data.repository.UserRepositoryImpl
import com.mustafa.influencer.domain.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random

// ViewModel UI State
data class InfluencerSearchUiState(
    val isLoading: Boolean = false,
    val allInfluencers: List<SearchInfluencer> = emptyList(),
    val filteredList: List<SearchInfluencer> = emptyList(),
    val searchQuery: String = "",
    val selectedCategory: String = "Tümü"
)

class InfluencerSearchViewModel(
    private val userRepo: UserRepository = UserRepositoryImpl()
) : ViewModel() {

    private val _state = MutableStateFlow(InfluencerSearchUiState())
    val state: StateFlow<InfluencerSearchUiState> = _state.asStateFlow()

    init {
        loadInfluencers()
    }

    private fun loadInfluencers() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)

            val userList = userRepo.getAllInfluencers()

            val uiList = userList.map { user ->

                // --- DÜZELTME BURADA ---
                // Eğer veritabanında isim (name) boşsa, mailin başındaki kısmı alıp baş harfini büyütüyor.
                val displayName = if (user.name.isNotBlank()) {
                    user.name
                } else {
                    // "mustafa@gmail.com" -> "Mustafa"
                    user.email.substringBefore("@").replaceFirstChar { it.uppercase() }
                }

                SearchInfluencer(
                    id = user.id,
                    name = displayName, // Düzeltilmiş isim buraya atanıyor
                    username = if (user.email.contains("@")) "@${user.email.split("@")[0]}" else "@user",
                    platform = user.platforms.firstOrNull() ?: "Instagram",
                    followers = if(user.followerCount.isNotBlank()) user.followerCount else "${Random.nextInt(10, 500)}K",
                    engagement = "${Random.nextDouble(1.0, 9.0).toString().take(3)}%",
                    category = user.categories.firstOrNull() ?: "Genel",
                    priceRange = if(user.priceRange.isNotBlank()) user.priceRange else "₺3,000 - ₺10,000",
                    isVerified = Random.nextBoolean(),
                    profileColor = generateRandomColor(),
                    bio = user.bio.ifBlank { "Henüz biyografi eklenmemiş." },
                    completedCampaigns = Random.nextInt(5, 50)
                )
            }

            _state.value = _state.value.copy(
                isLoading = false,
                allInfluencers = uiList,
                filteredList = uiList
            )
        }
    }

    fun onSearchQueryChange(query: String) {
        _state.value = _state.value.copy(searchQuery = query)
        applyFilters()
    }

    fun onCategorySelected(category: String) {
        _state.value = _state.value.copy(selectedCategory = category)
        applyFilters()
    }

    private fun applyFilters() {
        val s = _state.value
        val query = s.searchQuery.trim().lowercase()

        val filtered = s.allInfluencers.filter { influencer ->
            val matchesCategory = if (s.selectedCategory == "Tümü") true
            else influencer.category.equals(s.selectedCategory, ignoreCase = true)

            val matchesSearch = if (query.isEmpty()) true
            else (influencer.name.lowercase().contains(query) ||
                    influencer.username.lowercase().contains(query) ||
                    influencer.platform.lowercase().contains(query))

            matchesCategory && matchesSearch
        }

        _state.value = _state.value.copy(filteredList = filtered)
    }

    private fun generateRandomColor(): Color {
        val colors = listOf(
            Color(0xFFFF6B6B), Color(0xFF4ECDC4), Color(0xFFFFBE0B),
            Color(0xFF95E1D3), Color(0xFFE74C3C), Color(0xFF3498DB),
            Color(0xFF9B59B6), Color(0xFF1ABC9C)
        )
        return colors.random()
    }
}