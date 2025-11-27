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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

// Mock Data Models
data class PlatformStat(
    val platform: String,
    val followers: String,
    val engagement: String,
    val icon: ImageVector,
    val color: Color
)

data class MonthlyEarning(
    val month: String,
    val amount: String,
    val campaigns: Int
)

data class TopCampaign(
    val title: String,
    val views: String,
    val engagement: String,
    val earnings: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsScreen(
    onBackClick: () -> Unit
) {
    var selectedPeriod by remember { mutableStateOf("Bu Ay") }
    val periods = listOf("Bu Hafta", "Bu Ay", "Bu Yıl", "Tüm Zamanlar")

    // Mock Data
    val platformStats = listOf(
        PlatformStat(
            platform = "Instagram",
            followers = "25.4K",
            engagement = "4.8%",
            icon = Icons.Default.Camera,
            color = Color(0xFFE4405F)
        ),
        PlatformStat(
            platform = "YouTube",
            followers = "18.2K",
            engagement = "6.2%",
            icon = Icons.Default.PlayCircle,
            color = Color(0xFFFF0000)
        ),
        PlatformStat(
            platform = "TikTok",
            followers = "32.8K",
            engagement = "8.5%",
            icon = Icons.Default.MusicNote,
            color = Color(0xFF000000)
        ),
        PlatformStat(
            platform = "Twitter",
            followers = "12.1K",
            engagement = "3.2%",
            icon = Icons.Default.Chat,
            color = Color(0xFF1DA1F2)
        )
    )

    val monthlyEarnings = listOf(
        MonthlyEarning("Ocak", "₺3,200", 2),
        MonthlyEarning("Şubat", "₺5,800", 4),
        MonthlyEarning("Mart", "₺4,500", 3),
        MonthlyEarning("Nisan", "₺7,200", 5),
        MonthlyEarning("Mayıs", "₺8,450", 6),
        MonthlyEarning("Haziran", "₺6,100", 4)
    )

    val topCampaigns = listOf(
        TopCampaign(
            title = "Telefon İncelemesi",
            views = "125K",
            engagement = "8.2%",
            earnings = "₺12,000"
        ),
        TopCampaign(
            title = "Makyaj Ürünleri",
            views = "98K",
            engagement = "7.5%",
            earnings = "₺8,500"
        ),
        TopCampaign(
            title = "Oyun Yayını",
            views = "215K",
            engagement = "9.1%",
            earnings = "₺15,000"
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("İstatistikler") },
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
            // Period Selector
            item {
                PeriodSelectorSection(
                    periods = periods,
                    selectedPeriod = selectedPeriod,
                    onPeriodSelected = { selectedPeriod = it }
                )
            }

            // Overview Cards
            item {
                OverviewSection()
            }

            // Platform Statistics
            item {
                SectionHeader(
                    title = "Platform İstatistikleri",
                    icon = Icons.Default.TrendingUp
                )
            }
            item {
                PlatformStatsSection(platformStats)
            }

            // Monthly Earnings
            item {
                SectionHeader(
                    title = "Aylık Kazançlar",
                    icon = Icons.Default.AccountBalanceWallet
                )
            }
            item {
                MonthlyEarningsSection(monthlyEarnings)
            }

            // Top Campaigns
            item {
                SectionHeader(
                    title = "En İyi Performans",
                    icon = Icons.Default.Star
                )
            }
            items(topCampaigns) { campaign ->
                TopCampaignCard(campaign)
            }
        }
    }
}

@Composable
private fun PeriodSelectorSection(
    periods: List<String>,
    selectedPeriod: String,
    onPeriodSelected: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        periods.forEach { period ->
            FilterChip(
                selected = selectedPeriod == period,
                onClick = { onPeriodSelected(period) },
                label = { Text(period, style = MaterialTheme.typography.labelMedium) },
                leadingIcon = if (selectedPeriod == period) {
                    { Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp)) }
                } else null
            )
        }
    }
}

@Composable
private fun OverviewSection() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OverviewCard(
            title = "Toplam Görüntülenme",
            value = "458K",
            trend = "+18%",
            color = Color(0xFF6366F1),
            modifier = Modifier.weight(1f)
        )
        OverviewCard(
            title = "Ortalama Etkileşim",
            value = "6.4%",
            trend = "+2.1%",
            color = Color(0xFF8B5CF6),
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun OverviewCard(
    title: String,
    value: String,
    trend: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(120.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = color.copy(alpha = 0.1f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall,
                color = color
            )
            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = color
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.TrendingUp,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = Color(0xFF10B981)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = trend,
                    style = MaterialTheme.typography.labelSmall,
                    color = Color(0xFF10B981),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun SectionHeader(
    title: String,
    icon: ImageVector
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun PlatformStatsSection(platforms: List<PlatformStat>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        platforms.forEach { platform ->
            PlatformStatCard(platform)
        }
    }
    Spacer(modifier = Modifier.height(16.dp))
}

@Composable
private fun PlatformStatCard(platform: PlatformStat) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(platform.color.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = platform.icon,
                        contentDescription = null,
                        tint = platform.color,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Column {
                    Text(
                        text = platform.platform,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${platform.followers} takipçi",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = platform.engagement,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Etkileşim",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun MonthlyEarningsSection(earnings: List<MonthlyEarning>) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            earnings.forEach { earning ->
                MonthlyEarningRow(earning)
                if (earning != earnings.last()) {
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 12.dp),
                        color = MaterialTheme.colorScheme.outlineVariant
                    )
                }
            }
        }
    }
    Spacer(modifier = Modifier.height(16.dp))
}

@Composable
private fun MonthlyEarningRow(earning: MonthlyEarning) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = earning.month,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "${earning.campaigns} kampanya",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Text(
            text = earning.amount,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF10B981)
        )
    }
}

@Composable
private fun TopCampaignCard(campaign: TopCampaign) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 6.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = campaign.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Visibility,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = campaign.views,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.TrendingUp,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = campaign.engagement,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
            Text(
                text = campaign.earnings,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF10B981)
            )
        }
    }
}
