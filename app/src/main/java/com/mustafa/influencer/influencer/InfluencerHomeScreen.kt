package com.mustafa.influencer.influencer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

// Mock Data Models
data class StatCard(
    val title: String,
    val value: String,
    val icon: ImageVector,
    val color: Color,
    val trend: String = ""
)

data class AdvertiserCard(
    val companyName: String,
    val category: String,
    val budget: String,
    val platforms: List<String>,
    val description: String,
    val isVerified: Boolean = false
)

data class ActiveCampaign(
    val title: String,
    val advertiser: String,
    val status: String,
    val dueDate: String,
    val progress: Float
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InfluencerHomeScreen() {
    // Mock Data
    val stats = listOf(
        StatCard(
            title = "Görüntülenme",
            value = "12.5K",
            icon = Icons.Default.Visibility,
            color = Color(0xFF6366F1),
            trend = "+12%"
        ),
        StatCard(
            title = "Aktif Kampanya",
            value = "3",
            icon = Icons.Default.Campaign,
            color = Color(0xFF8B5CF6),
            trend = ""
        ),
        StatCard(
            title = "Toplam Kazanç",
            value = "₺8,450",
            icon = Icons.Default.AccountBalanceWallet,
            color = Color(0xFF10B981),
            trend = "+25%"
        )
    )

    val advertisers = listOf(
        AdvertiserCard(
            companyName = "TechVision A.Ş.",
            category = "Teknoloji",
            budget = "₺5,000 - ₺15,000",
            platforms = listOf("YouTube", "Instagram"),
            description = "Yeni ürün lansmanı için içerik üreticileri arıyoruz",
            isVerified = true
        ),
        AdvertiserCard(
            companyName = "BeautyPro",
            category = "Kozmetik",
            budget = "₺3,000 - ₺8,000",
            platforms = listOf("Instagram", "TikTok"),
            description = "Makyaj ürünlerimiz için sponsorlu içerik kampanyası",
            isVerified = true
        ),
        AdvertiserCard(
            companyName = "GameZone",
            category = "Oyun",
            budget = "₺10,000 - ₺25,000",
            platforms = listOf("YouTube", "Twitch"),
            description = "Yeni oyun tanıtımı için streaming kampanyası",
            isVerified = false
        ),
        AdvertiserCard(
            companyName = "FitLife",
            category = "Spor & Sağlık",
            budget = "₺4,000 - ₺12,000",
            platforms = listOf("Instagram", "YouTube"),
            description = "Spor ekipmanları ve sağlıklı yaşam içerikleri",
            isVerified = true
        )
    )

    val activeCampaigns = listOf(
        ActiveCampaign(
            title = "Yeni Telefon İncelemesi",
            advertiser = "TechVision A.Ş.",
            status = "Devam Ediyor",
            dueDate = "5 gün kaldı",
            progress = 0.65f
        ),
        ActiveCampaign(
            title = "Cilt Bakım Rutini",
            advertiser = "BeautyPro",
            status = "İncelemede",
            dueDate = "12 gün kaldı",
            progress = 0.30f
        )
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        // Welcome Header
        item {
            WelcomeHeader()
        }

        // Statistics Cards
        item {
            StatsSection(stats)
        }

        // Active Campaigns Section
        if (activeCampaigns.isNotEmpty()) {
            item {
                SectionHeader(
                    title = "Aktif Kampanyalarım",
                    subtitle = "${activeCampaigns.size} kampanya devam ediyor",
                    icon = Icons.Default.Rocket
                )
            }
            item {
                ActiveCampaignsSection(activeCampaigns)
            }
        }

        // Recommended Advertisers Section
        item {
            SectionHeader(
                title = "Önerilen İş Fırsatları",
                subtitle = "Sana uygun advertiser'lar",
                icon = Icons.Default.BusinessCenter
            )
        }
        item {
            RecommendedAdvertisersSection(advertisers)
        }

        // Quick Actions
        item {
            QuickActionsSection()
        }
    }
}

@Composable
private fun WelcomeHeader() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp),
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
                            MaterialTheme.colorScheme.primaryContainer,
                            MaterialTheme.colorScheme.secondaryContainer
                        )
                    )
                )
                .padding(24.dp)
        ) {
            Column {
                Text(
                    text = "Hoş Geldin! 👋",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Bugün yeni fırsatlar seni bekliyor",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                )
            }
        }
    }
}

@Composable
private fun StatsSection(stats: List<StatCard>) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(stats) { stat ->
            StatCardItem(stat)
        }
    }
}

@Composable
private fun StatCardItem(stat: StatCard) {
    Card(
        modifier = Modifier
            .width(160.dp)
            .height(120.dp),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
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
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(stat.color.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = stat.icon,
                    contentDescription = null,
                    tint = stat.color,
                    modifier = Modifier.size(24.dp)
                )
            }

            Column {
                Text(
                    text = stat.value,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = stat.title,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    if (stat.trend.isNotEmpty()) {
                        Text(
                            text = stat.trend,
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFF10B981),
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SectionHeader(
    title: String,
    subtitle: String,
    icon: ImageVector
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun ActiveCampaignsSection(campaigns: List<ActiveCampaign>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        campaigns.forEach { campaign ->
            ActiveCampaignCard(campaign)
        }
    }
    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
private fun ActiveCampaignCard(campaign: ActiveCampaign) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
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
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = campaign.advertiser,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
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
                        containerColor = Color(0xFF8B5CF6).copy(alpha = 0.1f),
                        labelColor = Color(0xFF8B5CF6)
                    )
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Schedule,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = campaign.dueDate,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            LinearProgressIndicator(
                progress = { campaign.progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = Color(0xFF8B5CF6),
                trackColor = Color(0xFF8B5CF6).copy(alpha = 0.1f)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "${(campaign.progress * 100).toInt()}% tamamlandı",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun RecommendedAdvertisersSection(advertisers: List<AdvertiserCard>) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(advertisers) { advertiser ->
            AdvertiserCardItem(advertiser)
        }
    }
    Spacer(modifier = Modifier.height(16.dp))
}

@Composable
private fun AdvertiserCardItem(advertiser: AdvertiserCard) {
    Card(
        modifier = Modifier
            .width(300.dp)
            .height(220.dp),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(
                                        Color(0xFF6366F1),
                                        Color(0xFF8B5CF6)
                                    )
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Business,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = advertiser.companyName,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            if (advertiser.isVerified) {
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(
                                    imageVector = Icons.Default.Verified,
                                    contentDescription = "Verified",
                                    modifier = Modifier.size(16.dp),
                                    tint = Color(0xFF10B981)
                                )
                            }
                        }
                        Text(
                            text = advertiser.category,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = advertiser.description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                advertiser.platforms.take(3).forEach { platform ->
                    AssistChip(
                        onClick = { },
                        label = {
                            Text(
                                text = platform,
                                style = MaterialTheme.typography.labelSmall
                            )
                        },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer,
                            labelColor = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

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
                        text = advertiser.budget,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF10B981)
                    )
                }
                FilledTonalButton(
                    onClick = { },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text("İncele")
                }
            }
        }
    }
}

@Composable
private fun QuickActionsSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp)
    ) {
        Text(
            text = "Hızlı İşlemler",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            QuickActionCard(
                title = "Kampanya Ara",
                icon = Icons.Default.Search,
                color = Color(0xFF6366F1),
                modifier = Modifier.weight(1f)
            )
            QuickActionCard(
                title = "Mesajlar",
                icon = Icons.Default.Message,
                color = Color(0xFF8B5CF6),
                modifier = Modifier.weight(1f)
            )
            QuickActionCard(
                title = "İstatistikler",
                icon = Icons.Default.BarChart,
                color = Color(0xFF10B981),
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun QuickActionCard(
    title: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(90.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = color.copy(alpha = 0.1f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                color = color,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
