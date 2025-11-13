package com.mustafa.influencer.advertiser

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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

// Mock Data Models for Advertiser
data class CampaignStat(
    val title: String,
    val value: String,
    val icon: ImageVector,
    val color: Color,
    val subtitle: String = ""
)

data class InfluencerCard(
    val name: String,
    val username: String,
    val platform: String,
    val followers: String,
    val engagement: String,
    val category: String,
    val priceRange: String,
    val isVerified: Boolean = false,
    val profileColor: Color
)

data class ActiveCampaignAd(
    val title: String,
    val influencerCount: Int,
    val applicants: Int,
    val budget: String,
    val status: String,
    val deadline: String,
    val reach: String
)

data class ApplicationPending(
    val influencerName: String,
    val campaign: String,
    val platform: String,
    val followers: String,
    val requestedAmount: String,
    val appliedDate: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdvertiserHomeScreen(
    onInfluencerSearchClick: () -> Unit = {},
    onCreateCampaignClick: () -> Unit = {},
    onReportsClick: () -> Unit = {}
) {
    // Mock Data
    val campaignStats = listOf(
        CampaignStat(
            title = "Aktif Kampanya",
            value = "5",
            icon = Icons.Default.Campaign,
            color = Color(0xFF6366F1),
            subtitle = "2 yeni"
        ),
        CampaignStat(
            title = "Toplam Erişim",
            value = "1.2M",
            icon = Icons.Default.Visibility,
            color = Color(0xFF8B5CF6),
            subtitle = "+18% bu ay"
        ),
        CampaignStat(
            title = "Bütçe",
            value = "₺45K",
            icon = Icons.Default.AccountBalanceWallet,
            color = Color(0xFF10B981),
            subtitle = "₺22K kalan"
        )
    )

    val recommendedInfluencers = listOf(
        InfluencerCard(
            name = "Ayşe Yılmaz",
            username = "@ayse_tech",
            platform = "YouTube",
            followers = "125K",
            engagement = "6.8%",
            category = "Teknoloji",
            priceRange = "₺5,000 - ₺12,000",
            isVerified = true,
            profileColor = Color(0xFFFF6B6B)
        ),
        InfluencerCard(
            name = "Mehmet Kaya",
            username = "@mehmet_gaming",
            platform = "Twitch",
            followers = "89K",
            engagement = "8.2%",
            category = "Oyun",
            priceRange = "₺8,000 - ₺15,000",
            isVerified = true,
            profileColor = Color(0xFF4ECDC4)
        ),
        InfluencerCard(
            name = "Zeynep Demir",
            username = "@zeynep_beauty",
            platform = "Instagram",
            followers = "210K",
            engagement = "5.4%",
            category = "Makyaj",
            priceRange = "₺10,000 - ₺20,000",
            isVerified = true,
            profileColor = Color(0xFFFFBE0B)
        ),
        InfluencerCard(
            name = "Can Öztürk",
            username = "@can_fitness",
            platform = "Instagram",
            followers = "67K",
            engagement = "7.1%",
            category = "Spor",
            priceRange = "₺3,000 - ₺8,000",
            isVerified = false,
            profileColor = Color(0xFF95E1D3)
        )
    )

    val activeCampaigns = listOf(
        ActiveCampaignAd(
            title = "Yeni Ürün Lansmanı 2024",
            influencerCount = 3,
            applicants = 12,
            budget = "₺25,000",
            status = "Aktif",
            deadline = "15 gün kaldı",
            reach = "450K"
        ),
        ActiveCampaignAd(
            title = "Bahar Koleksiyonu Tanıtımı",
            influencerCount = 2,
            applicants = 8,
            budget = "₺15,000",
            status = "İncelemede",
            deadline = "22 gün kaldı",
            reach = "320K"
        )
    )

    val pendingApplications = listOf(
        ApplicationPending(
            influencerName = "Selin Arslan",
            campaign = "Yeni Ürün Lansmanı 2024",
            platform = "YouTube",
            followers = "95K",
            requestedAmount = "₺8,500",
            appliedDate = "2 gün önce"
        ),
        ApplicationPending(
            influencerName = "Burak Çelik",
            campaign = "Bahar Koleksiyonu Tanıtımı",
            platform = "Instagram",
            followers = "156K",
            requestedAmount = "₺12,000",
            appliedDate = "5 saat önce"
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
            AdvertiserWelcomeHeader()
        }

        // Campaign Statistics
        item {
            CampaignStatsSection(campaignStats)
        }

        // Quick Actions
        item {
            AdvertiserQuickActions(
                onInfluencerSearchClick = onInfluencerSearchClick,
                onCreateCampaignClick = onCreateCampaignClick,
                onReportsClick = onReportsClick
            )
        }

        // Pending Applications Section
        if (pendingApplications.isNotEmpty()) {
            item {
                AdSectionHeader(
                    title = "Bekleyen Başvurular",
                    subtitle = "${pendingApplications.size} influencer onay bekliyor",
                    icon = Icons.Default.HourglassEmpty
                )
            }
            item {
                PendingApplicationsSection(pendingApplications)
            }
        }

        // Active Campaigns Section
        if (activeCampaigns.isNotEmpty()) {
            item {
                AdSectionHeader(
                    title = "Aktif Kampanyalarım",
                    subtitle = "${activeCampaigns.size} kampanya devam ediyor",
                    icon = Icons.Default.Rocket
                )
            }
            item {
                ActiveCampaignsAdSection(activeCampaigns)
            }
        }

        // Recommended Influencers Section
        item {
            AdSectionHeader(
                title = "Önerilen İçerik Üreticiler",
                subtitle = "Şirket profilinize uygun influencer'lar",
                icon = Icons.Default.Stars
            )
        }
        item {
            RecommendedInfluencersSection(recommendedInfluencers)
        }

        // Campaign Performance Insights
        item {
            CampaignInsightsSection()
        }
    }
}

@Composable
private fun AdvertiserWelcomeHeader() {
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
                            Color(0xFF6366F1),
                            Color(0xFF8B5CF6)
                        )
                    )
                )
                .padding(24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Kampanyalarınız Aktif 🚀",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Yeni başvurular ve güncellemeler sizi bekliyor",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.TrendingUp,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun CampaignStatsSection(stats: List<CampaignStat>) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(stats) { stat ->
            CampaignStatCard(stat)
        }
    }
    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
private fun CampaignStatCard(stat: CampaignStat) {
    Card(
        modifier = Modifier
            .width(180.dp)
            .height(130.dp),
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
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
            }

            Column {
                Text(
                    text = stat.value,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = stat.title,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (stat.subtitle.isNotEmpty()) {
                    Text(
                        text = stat.subtitle,
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFF10B981),
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Composable
private fun AdvertiserQuickActions(
    onInfluencerSearchClick: () -> Unit = {},
    onCreateCampaignClick: () -> Unit = {},
    onReportsClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp)
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
            AdQuickActionCard(
                title = "Kampanya Oluştur",
                icon = Icons.Default.Add,
                color = Color(0xFF6366F1),
                onClick = onCreateCampaignClick,
                modifier = Modifier.weight(1f)
            )
            AdQuickActionCard(
                title = "İnfluencer Ara",
                icon = Icons.Default.Search,
                color = Color(0xFF8B5CF6),
                onClick = onInfluencerSearchClick,
                modifier = Modifier.weight(1f)
            )
            AdQuickActionCard(
                title = "Raporlar",
                icon = Icons.Default.Assessment,
                color = Color(0xFF10B981),
                onClick = onReportsClick,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun AdQuickActionCard(
    title: String,
    icon: ImageVector,
    color: Color,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(95.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = color.copy(alpha = 0.1f)
        ),
        onClick = onClick
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
                modifier = Modifier.size(32.dp)
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

@Composable
private fun AdSectionHeader(
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
private fun PendingApplicationsSection(applications: List<ApplicationPending>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        applications.forEach { application ->
            PendingApplicationCard(application)
        }
    }
    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
private fun PendingApplicationCard(application: ApplicationPending) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFF4E6)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
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
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFF59E0B).copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = Color(0xFFF59E0B),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = application.influencerName,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "${application.platform} • ${application.followers} takipçi",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                AssistChip(
                    onClick = { },
                    label = {
                        Text(
                            text = application.appliedDate,
                            style = MaterialTheme.typography.labelSmall
                        )
                    },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = Color(0xFFF59E0B).copy(alpha = 0.1f),
                        labelColor = Color(0xFFF59E0B)
                    )
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = application.campaign,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Talep Edilen",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = application.requestedAmount,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF10B981)
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilledTonalButton(
                        onClick = { },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.filledTonalButtonColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer,
                            contentColor = MaterialTheme.colorScheme.onErrorContainer
                        )
                    ) {
                        Text("Reddet")
                    }
                    FilledTonalButton(
                        onClick = { },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.filledTonalButtonColors(
                            containerColor = Color(0xFF10B981),
                            contentColor = Color.White
                        )
                    ) {
                        Text("Onayla")
                    }
                }
            }
        }
    }
}

@Composable
private fun ActiveCampaignsAdSection(campaigns: List<ActiveCampaignAd>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        campaigns.forEach { campaign ->
            ActiveCampaignAdCard(campaign)
        }
    }
    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
private fun ActiveCampaignAdCard(campaign: ActiveCampaignAd) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
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
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(4.dp))
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

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                CampaignMetricItem(
                    icon = Icons.Default.People,
                    label = "İnfluencer",
                    value = campaign.influencerCount.toString(),
                    color = Color(0xFF6366F1)
                )
                CampaignMetricItem(
                    icon = Icons.Default.Mail,
                    label = "Başvuru",
                    value = campaign.applicants.toString(),
                    color = Color(0xFF8B5CF6)
                )
                CampaignMetricItem(
                    icon = Icons.Default.Visibility,
                    label = "Erişim",
                    value = campaign.reach,
                    color = Color(0xFF10B981)
                )
                CampaignMetricItem(
                    icon = Icons.Default.Payments,
                    label = "Bütçe",
                    value = campaign.budget,
                    color = Color(0xFFF59E0B)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            FilledTonalButton(
                onClick = { },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Kampanya Detayları")
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@Composable
private fun CampaignMetricItem(
    icon: ImageVector,
    label: String,
    value: String,
    color: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun RecommendedInfluencersSection(influencers: List<InfluencerCard>) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(influencers) { influencer ->
            RecommendedInfluencerCard(influencer)
        }
    }
    Spacer(modifier = Modifier.height(16.dp))
}

@Composable
private fun RecommendedInfluencerCard(influencer: InfluencerCard) {
    Card(
        modifier = Modifier
            .width(280.dp)
            .height(260.dp),
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
                verticalAlignment = Alignment.Top
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(
                                        influencer.profileColor,
                                        influencer.profileColor.copy(alpha = 0.6f)
                                    )
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = influencer.name,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            if (influencer.isVerified) {
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
                            text = influencer.username,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Takipçi",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = influencer.followers,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "Etkileşim",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = influencer.engagement,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF10B981)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                AssistChip(
                    onClick = { },
                    label = {
                        Text(
                            text = influencer.platform,
                            style = MaterialTheme.typography.labelSmall
                        )
                    },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        labelColor = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                )
                AssistChip(
                    onClick = { },
                    label = {
                        Text(
                            text = influencer.category,
                            style = MaterialTheme.typography.labelSmall
                        )
                    },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                        labelColor = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Divider(modifier = Modifier.padding(vertical = 12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Fiyat Aralığı",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = influencer.priceRange,
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
private fun CampaignInsightsSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp)
    ) {
        Text(
            text = "Kampanya Performansı",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    InsightMetric(
                        label = "Ortalama Erişim",
                        value = "385K",
                        trend = "+12%",
                        isPositive = true
                    )
                    InsightMetric(
                        label = "Etkileşim Oranı",
                        value = "6.4%",
                        trend = "+8%",
                        isPositive = true
                    )
                    InsightMetric(
                        label = "ROI",
                        value = "3.2x",
                        trend = "+15%",
                        isPositive = true
                    )
                }
            }
        }
    }
}

@Composable
private fun InsightMetric(
    label: String,
    value: String,
    trend: String,
    isPositive: Boolean
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = trend,
            style = MaterialTheme.typography.labelSmall,
            color = if (isPositive) Color(0xFF10B981) else Color(0xFFEF4444),
            fontWeight = FontWeight.SemiBold
        )
    }
}
