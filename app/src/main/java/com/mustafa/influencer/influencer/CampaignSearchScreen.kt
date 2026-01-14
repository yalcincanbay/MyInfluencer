package com.mustafa.influencer.influencer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mustafa.influencer.domain.model.Campaign

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CampaignSearchScreen(
    onBackClick: () -> Unit,
    onCampaignClick: (String) -> Unit = {},
    vm: CampaignSearchViewModel = viewModel()
) {
    // 1. State'i dinliyoruz
    val uiState by vm.state.collectAsState()

    // 2. Sayfa açılınca verileri yenile
    LaunchedEffect(Unit) {
        vm.refresh()
    }

    // 3. FİLTRELEME MANTIĞINI BURAYA ALDIK (ViewModel'e güvenmiyoruz)
    // uiState her değiştiğinde bu blok yeniden hesaplanır.
    val filteredList = remember(uiState.all, uiState.searchQuery, uiState.selectedCategory) {
        if (uiState.all.isEmpty()) {
            emptyList()
        } else {
            val query = uiState.searchQuery.trim()
            uiState.all.filter { campaign ->
                // Kategori Eşleşmesi
                val isCategoryMatch = if (uiState.selectedCategory == "Tümü") true
                else campaign.category.equals(uiState.selectedCategory, ignoreCase = true)

                // Arama Eşleşmesi (Başlık veya Marka Adı)
                val isSearchMatch = if (query.isEmpty()) true
                else (campaign.title.contains(query, ignoreCase = true) ||
                        campaign.advertiserName.contains(query, ignoreCase = true))

                isCategoryMatch && isSearchMatch
            }
        }
    }

    val categories = listOf("Tümü", "Teknoloji", "Oyun", "Makyaj", "Spor", "Yemek", "Moda", "Seyahat", "Eğitim")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Kampanya Ara") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Geri")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            // --- DEBUG BÖLÜMÜ (Veriler gelince kaldırabilirsin) ---
            item {
                if (uiState.all.isNotEmpty() && filteredList.isEmpty()) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE)),
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Column(Modifier.padding(10.dp)) {
                            Text("⚠️ DEBUG BİLGİSİ:", fontWeight = FontWeight.Bold, color = Color.Red)
                            Text("Firebase'den Gelen: ${uiState.all.size} adet")
                            Text("Filtre Sonrası: 0 adet")
                            Text("Seçili Kategori: '${uiState.selectedCategory}'")
                            Text("Aranan Kelime: '${uiState.searchQuery}'")

                            // İlk verinin neden elendiğini gösterelim
                            val ornek = uiState.all.first()
                            Text("--- İlk Veri Analizi ---")
                            Text("Başlık: ${ornek.title}")
                            Text("Kategori: '${ornek.category}'")

                            val catMatch = (uiState.selectedCategory == "Tümü" || ornek.category.equals(uiState.selectedCategory, true))
                            Text("Kategori Tutuyor mu?: $catMatch")
                        }
                    }
                }
            }
            // -----------------------------------------------------

            // Arama Çubuğu
            item {
                OutlinedTextField(
                    value = uiState.searchQuery,
                    onValueChange = vm::setQuery,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 12.dp),
                    placeholder = { Text("Kampanya veya marka ara...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    trailingIcon = {
                        if (uiState.searchQuery.isNotEmpty()) {
                            IconButton(onClick = { vm.setQuery("") }) {
                                Icon(Icons.Default.Clear, contentDescription = "Temizle")
                            }
                        }
                    },
                    shape = RoundedCornerShape(16.dp),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface
                    )
                )
            }

            // Kategoriler
            item {
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(categories) { cat ->
                        FilterChip(
                            selected = uiState.selectedCategory == cat,
                            onClick = { vm.setCategory(cat) },
                            label = { Text(cat) },
                            leadingIcon = if (uiState.selectedCategory == cat) {
                                { Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp)) }
                            } else null
                        )
                    }
                }
            }

            // Yükleniyor
            item {
                if (uiState.isLoading) {
                    Box(Modifier.fillMaxWidth().padding(30.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
            }

            // Sonuç Sayısı
            item {
                if (!uiState.isLoading && uiState.errorMessage.isBlank()) {
                    Text(
                        text = "${filteredList.size} kampanya bulundu",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
                    )
                }
            }

            // Liste
            items(filteredList) { campaign ->
                CampaignCard(
                    campaign = campaign,
                    onClick = { onCampaignClick(campaign.id) }
                )
            }
        }
    }
}

@Composable
private fun CampaignCard(
    campaign: Campaign,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = campaign.title,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = campaign.advertiserName.ifBlank { "Marka" },
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            if (campaign.description.isNotBlank()) {
                Text(
                    text = campaign.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                if (campaign.platform.isNotBlank()) {
                    AssistChip(
                        onClick = { },
                        label = { Text(campaign.platform) },
                        colors = AssistChipDefaults.assistChipColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    )
                }
                AssistChip(onClick = { }, label = { Text(campaign.category.ifBlank { "Genel" }) })
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Bütçe", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(
                        text = "₺${campaign.budget}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF10B981)
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Schedule, contentDescription = null, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = campaign.deadlineText.ifBlank { "-" },
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}