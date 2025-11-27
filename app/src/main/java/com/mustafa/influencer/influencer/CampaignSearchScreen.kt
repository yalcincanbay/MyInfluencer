package com.mustafa.influencer.influencer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

// Mock Data Model
data class Campaign(
    val title: String,
    val advertiser: String,
    val category: String,
    val budget: String,
    val description: String,
    val platforms: List<String>,
    val requirements: String,
    val deadline: String,
    val isNew: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CampaignSearchScreen(
    onBackClick: () -> Unit,
    onCampaignClick: (String) -> Unit = {}
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Tümü") }

    val categories = listOf("Tümü", "Teknoloji", "Kozmetik", "Oyun", "Moda", "Yemek", "Spor")

    // Mock Campaigns
    val allCampaigns = listOf(
        Campaign(
            title = "Akıllı Telefon İncelemesi",
            advertiser = "TechVision A.Ş.",
            category = "Teknoloji",
            budget = "₺8,000 - ₺15,000",
            description = "Yeni çıkan flagship telefonumuz için detaylı inceleme videosu",
            platforms = listOf("YouTube", "Instagram"),
            requirements = "10K+ takipçi, teknoloji içeriği",
            deadline = "15 gün",
            isNew = true
        ),
        Campaign(
            title = "Makyaj Ürünleri Tanıtımı",
            advertiser = "BeautyPro",
            category = "Kozmetik",
            budget = "₺4,000 - ₺8,000",
            description = "Yeni makyaj serimizin tanıtımı için sponsorlu içerik",
            platforms = listOf("Instagram", "TikTok"),
            requirements = "5K+ takipçi, kozmetik içeriği",
            deadline = "20 gün"
        ),
        Campaign(
            title = "Oyun Yayını Kampanyası",
            advertiser = "GameZone",
            category = "Oyun",
            budget = "₺12,000 - ₺25,000",
            description = "Yeni oyun lansmanı için canlı yayın ve gameplay videoları",
            platforms = listOf("Twitch", "YouTube"),
            requirements = "15K+ takipçi, gaming içeriği",
            deadline = "10 gün",
            isNew = true
        ),
        Campaign(
            title = "Fitness Challenge",
            advertiser = "FitLife",
            category = "Spor",
            budget = "₺5,000 - ₺10,000",
            description = "30 günlük fitness challenge için içerik serisi",
            platforms = listOf("Instagram", "YouTube"),
            requirements = "8K+ takipçi, spor içeriği",
            deadline = "25 gün"
        ),
        Campaign(
            title = "Moda Koleksiyonu Tanıtımı",
            advertiser = "StyleHub",
            category = "Moda",
            budget = "₺6,000 - ₺12,000",
            description = "Yeni sezon koleksiyonumuz için lookbook çekimi",
            platforms = listOf("Instagram", "TikTok"),
            requirements = "12K+ takipçi, moda içeriği",
            deadline = "18 gün"
        ),
        Campaign(
            title = "Restoran Tanıtımı",
            advertiser = "FoodieSpot",
            category = "Yemek",
            budget = "₺3,000 - ₺6,000",
            description = "Yeni açılan restoranımız için tanıtım içeriği",
            platforms = listOf("Instagram", "YouTube"),
            requirements = "7K+ takipçi, yemek içeriği",
            deadline = "12 gün"
        )
    )

    val filteredCampaigns = allCampaigns.filter {
        (selectedCategory == "Tümü" || it.category == selectedCategory) &&
                (searchQuery.isEmpty() || it.title.contains(searchQuery, ignoreCase = true) ||
                        it.advertiser.contains(searchQuery, ignoreCase = true))
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Kampanya Ara") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Geri")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            // Search Bar
            item {
                SearchSection(
                    searchQuery = searchQuery,
                    onSearchQueryChange = { searchQuery = it }
                )
            }

            // Category Chips
            item {
                CategoryChipsSection(
                    categories = categories,
                    selectedCategory = selectedCategory,
                    onCategorySelected = { selectedCategory = it }
                )
            }

            // Results Header
            item {
                Text(
                    text = "${filteredCampaigns.size} kampanya bulundu",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)
                )
            }

            // Campaign List
            items(filteredCampaigns) { campaign ->
                CampaignCard(
                    campaign = campaign,
                    onClick = { onCampaignClick(campaign.title) }
                )
            }
        }
    }
}

@Composable
private fun SearchSection(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit
) {
    OutlinedTextField(
        value = searchQuery,
        onValueChange = onSearchQueryChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp),
        placeholder = { Text("Kampanya veya advertiser ara...") },
        leadingIcon = {
            Icon(Icons.Default.Search, contentDescription = null)
        },
        trailingIcon = {
            if (searchQuery.isNotEmpty()) {
                IconButton(onClick = { onSearchQueryChange("") }) {
                    Icon(Icons.Default.Clear, contentDescription = "Temizle")
                }
            }
        },
        shape = RoundedCornerShape(16.dp),
        singleLine = true
    )
}

@Composable
private fun CategoryChipsSection(
    categories: List<String>,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        categories.take(4).forEach { category ->
            FilterChip(
                selected = selectedCategory == category,
                onClick = { onCategorySelected(category) },
                label = { Text(category) },
                leadingIcon = if (selectedCategory == category) {
                    { Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp)) }
                } else null
            )
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
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = campaign.title,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        if (campaign.isNew) {
                            Spacer(modifier = Modifier.width(8.dp))
                            AssistChip(
                                onClick = { },
                                label = { Text("YENİ", style = MaterialTheme.typography.labelSmall) },
                                colors = AssistChipDefaults.assistChipColors(
                                    containerColor = Color(0xFFEF4444).copy(alpha = 0.1f),
                                    labelColor = Color(0xFFEF4444)
                                ),
                                modifier = Modifier.height(24.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = campaign.advertiser,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Description
            Text(
                text = campaign.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Platforms
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                campaign.platforms.forEach { platform ->
                    AssistChip(
                        onClick = { },
                        label = { Text(platform, style = MaterialTheme.typography.labelSmall) },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer,
                            labelColor = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    )
                }
                AssistChip(
                    onClick = { },
                    label = { Text(campaign.category, style = MaterialTheme.typography.labelSmall) },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                        labelColor = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Info Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Bütçe",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = campaign.budget,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF10B981)
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Schedule,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = campaign.deadline,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Requirements
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = campaign.requirements,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
