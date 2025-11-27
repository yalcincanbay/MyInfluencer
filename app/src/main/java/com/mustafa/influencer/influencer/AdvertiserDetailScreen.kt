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

// Mock Data Models
data class AdvertiserDetailInfo(
    val companyName: String,
    val category: String,
    val description: String,
    val totalCampaigns: String,
    val avgBudget: String,
    val rating: Float,
    val isVerified: Boolean,
    val about: String,
    val platforms: List<String>,
    val workingWith: List<String>
)

data class AdvertiserCampaign(
    val title: String,
    val budget: String,
    val deadline: String,
    val status: String,
    val platforms: List<String>
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdvertiserDetailScreen(
    advertiserId: String,
    onBackClick: () -> Unit,
    onMessageClick: () -> Unit = {},
    onCampaignClick: (String) -> Unit = {}
) {
    // Mock Advertiser Detail
    val advertiser = AdvertiserDetailInfo(
        companyName = "TechVision A.Ş.",
        category = "Teknoloji",
        description = "Türkiye'nin önde gelen teknoloji şirketlerinden",
        totalCampaigns = "48",
        avgBudget = "₺12,000",
        rating = 4.8f,
        isVerified = true,
        about = "TechVision A.Ş. olarak 2015 yılından beri teknoloji sektöründe faaliyet göstermekteyiz. Akıllı telefon, bilgisayar, akıllı saat ve diğer teknoloji ürünlerinin tanıtımı için içerik üreticileriyle işbirliği yapıyoruz. Kaliteli ve özgün içerikler üreten influencer'larla çalışmayı tercih ediyoruz.",
        platforms = listOf("YouTube", "Instagram", "Twitter", "TikTok"),
        workingWith = listOf("Teknoloji", "Gaming", "Lifestyle")
    )

    val campaigns = listOf(
        AdvertiserCampaign(
            title = "Akıllı Telefon İncelemesi",
            budget = "₺8,000 - ₺15,000",
            deadline = "15 gün",
            status = "Aktif",
            platforms = listOf("YouTube", "Instagram")
        ),
        AdvertiserCampaign(
            title = "Laptop Performans Testi",
            budget = "₺10,000 - ₺20,000",
            deadline = "20 gün",
            status = "Aktif",
            platforms = listOf("YouTube")
        ),
        AdvertiserCampaign(
            title = "Akıllı Saat Tanıtımı",
            budget = "₺5,000 - ₺10,000",
            deadline = "25 gün",
            status = "Aktif",
            platforms = listOf("Instagram", "TikTok")
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Advertiser Detayı") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Geri")
                    }
                },
                actions = {
                    IconButton(onClick = { /* Share */ }) {
                        Icon(Icons.Default.Share, contentDescription = "Paylaş")
                    }
                    IconButton(onClick = { /* Favorite */ }) {
                        Icon(Icons.Default.FavoriteBorder, contentDescription = "Favorilere Ekle")
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
            // Header Card
            item {
                AdvertiserHeaderCard(advertiser)
            }

            // Stats Row
            item {
                StatsRow(advertiser)
            }

            // Action Buttons
            item {
                ActionButtonsRow(onMessageClick)
            }

            // About Section
            item {
                AboutSection(advertiser.about)
            }

            // Platforms
            item {
                PlatformsSection(advertiser.platforms)
            }

            // Working With
            item {
                WorkingWithSection(advertiser.workingWith)
            }

            // Active Campaigns
            item {
                SectionTitle("Aktif Kampanyalar")
            }
            items(campaigns) { campaign ->
                AdvertiserCampaignCard(
                    campaign = campaign,
                    onClick = { onCampaignClick(campaign.title) }
                )
            }
        }
    }
}

@Composable
private fun AdvertiserHeaderCard(advertiser: AdvertiserDetailInfo) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFF6366F1),
                            Color(0xFF8B5CF6)
                        )
                    )
                )
                .padding(24.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Avatar
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Business,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(40.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Company Name
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = advertiser.companyName,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    if (advertiser.isVerified) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.Default.Verified,
                            contentDescription = "Verified",
                            modifier = Modifier.size(24.dp),
                            tint = Color(0xFF10B981)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Category
                AssistChip(
                    onClick = { },
                    label = {
                        Text(
                            text = advertiser.category,
                            style = MaterialTheme.typography.labelMedium
                        )
                    },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = Color.White.copy(alpha = 0.2f),
                        labelColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Rating
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = Color(0xFFFBBF24),
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = advertiser.rating.toString(),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "(${advertiser.totalCampaigns} kampanya)",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }
            }
        }
    }
}

@Composable
private fun StatsRow(advertiser: AdvertiserDetailInfo) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        StatCard(
            title = "Toplam Kampanya",
            value = advertiser.totalCampaigns,
            icon = Icons.Default.Campaign,
            color = Color(0xFF6366F1),
            modifier = Modifier.weight(1f)
        )
        StatCard(
            title = "Ort. Bütçe",
            value = advertiser.avgBudget,
            icon = Icons.Default.AccountBalanceWallet,
            color = Color(0xFF10B981),
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun StatCard(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(100.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(24.dp)
            )
            Column {
                Text(
                    text = value,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun ActionButtonsRow(onMessageClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Button(
            onClick = onMessageClick,
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(Icons.Default.Message, contentDescription = null, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Mesaj Gönder")
        }
        OutlinedButton(
            onClick = { /* Follow */ },
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Takip Et")
        }
    }
}

@Composable
private fun AboutSection(about: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp)
    ) {
        Text(
            text = "Hakkında",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Text(
                text = about,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Composable
private fun PlatformsSection(platforms: List<String>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp)
    ) {
        Text(
            text = "Platformlar",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            platforms.forEach { platform ->
                AssistChip(
                    onClick = { },
                    label = { Text(platform, style = MaterialTheme.typography.labelMedium) },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        labelColor = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                )
            }
        }
    }
}

@Composable
private fun WorkingWithSection(categories: List<String>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp)
    ) {
        Text(
            text = "İlgilendiği Kategoriler",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            categories.forEach { category ->
                AssistChip(
                    onClick = { },
                    label = { Text(category, style = MaterialTheme.typography.labelMedium) },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                        labelColor = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                )
            }
        }
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)
    )
}

@Composable
private fun AdvertiserCampaignCard(
    campaign: AdvertiserCampaign,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 6.dp),
        shape = RoundedCornerShape(16.dp),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = campaign.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = campaign.budget,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF10B981),
                        fontWeight = FontWeight.SemiBold
                    )
                }
                AssistChip(
                    onClick = { },
                    label = {
                        Text(
                            text = campaign.status,
                            style = MaterialTheme.typography.labelSmall
                        )
                    },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = Color(0xFF10B981).copy(alpha = 0.1f),
                        labelColor = Color(0xFF10B981)
                    )
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
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
        }
    }
}
