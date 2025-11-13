package com.mustafa.influencer.advertiser

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

data class ReportSummary(
    val title: String,
    val value: String,
    val change: String,
    val isPositive: Boolean,
    val icon: ImageVector,
    val color: Color
)

data class CampaignReport(
    val campaignName: String,
    val status: String,
    val startDate: String,
    val endDate: String,
    val totalReach: String,
    val engagement: String,
    val roi: String,
    val spent: String,
    val influencersCount: Int
)

data class PerformanceMetric(
    val label: String,
    val current: String,
    val previous: String,
    val change: String,
    val isPositive: Boolean
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportsScreen(
    onBackClick: () -> Unit
) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Genel Bakış", "Kampanyalar", "Performans")

    // Mock Data
    val summaryData = listOf(
        ReportSummary(
            title = "Toplam Erişim",
            value = "2.4M",
            change = "+24%",
            isPositive = true,
            icon = Icons.Default.Visibility,
            color = Color(0xFF6366F1)
        ),
        ReportSummary(
            title = "Etkileşim Oranı",
            value = "7.2%",
            change = "+12%",
            isPositive = true,
            icon = Icons.Default.TrendingUp,
            color = Color(0xFF8B5CF6)
        ),
        ReportSummary(
            title = "Toplam Harcama",
            value = "₺125K",
            change = "+8%",
            isPositive = false,
            icon = Icons.Default.Payments,
            color = Color(0xFFF59E0B)
        ),
        ReportSummary(
            title = "ROI",
            value = "3.8x",
            change = "+18%",
            isPositive = true,
            icon = Icons.Default.ShowChart,
            color = Color(0xFF10B981)
        )
    )

    val campaignReports = listOf(
        CampaignReport(
            campaignName = "Yeni Ürün Lansmanı 2024",
            status = "Tamamlandı",
            startDate = "01 Oca 2024",
            endDate = "15 Şub 2024",
            totalReach = "850K",
            engagement = "6.8%",
            roi = "4.2x",
            spent = "₺45,000",
            influencersCount = 5
        ),
        CampaignReport(
            campaignName = "Bahar Koleksiyonu",
            status = "Devam Ediyor",
            startDate = "20 Şub 2024",
            endDate = "30 Mar 2024",
            totalReach = "520K",
            engagement = "7.5%",
            roi = "3.6x",
            spent = "₺32,000",
            influencersCount = 4
        ),
        CampaignReport(
            campaignName = "Black Friday Özel",
            status = "Tamamlandı",
            startDate = "15 Kas 2023",
            endDate = "30 Kas 2023",
            totalReach = "1.2M",
            engagement = "8.2%",
            roi = "5.1x",
            spent = "₺65,000",
            influencersCount = 8
        )
    )

    val performanceMetrics = listOf(
        PerformanceMetric(
            label = "Ortalama Tıklama Oranı",
            current = "4.2%",
            previous = "3.8%",
            change = "+10.5%",
            isPositive = true
        ),
        PerformanceMetric(
            label = "Dönüşüm Oranı",
            current = "2.8%",
            previous = "2.5%",
            change = "+12%",
            isPositive = true
        ),
        PerformanceMetric(
            label = "Ortalama Sipariş Değeri",
            current = "₺425",
            previous = "₺380",
            change = "+11.8%",
            isPositive = true
        ),
        PerformanceMetric(
            label = "Müşteri Kazanım Maliyeti",
            current = "₺68",
            previous = "₺75",
            change = "-9.3%",
            isPositive = true
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Raporlar",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Geri"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* Download report */ }) {
                        Icon(
                            imageVector = Icons.Default.Download,
                            contentDescription = "İndir"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Tabs
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.primary
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = {
                            Text(
                                text = title,
                                fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    )
                }
            }

            // Content
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                when (selectedTab) {
                    0 -> {
                        // Overview Tab
                        item {
                            Text(
                                text = "Son 30 Gün Özeti",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        items(summaryData.chunked(2)) { rowItems ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                rowItems.forEach { summary ->
                                    SummaryCard(
                                        summary = summary,
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }
                        }

                        item {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Kampanya Dağılımı",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        item {
                            CampaignDistributionCard()
                        }

                        item {
                            Text(
                                text = "Platform Analizi",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        item {
                            PlatformAnalysisCard()
                        }
                    }

                    1 -> {
                        // Campaigns Tab
                        item {
                            Text(
                                text = "Kampanya Raporları",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        items(campaignReports) { campaign ->
                            CampaignReportCard(campaign)
                        }
                    }

                    2 -> {
                        // Performance Tab
                        item {
                            Text(
                                text = "Performans Metrikleri",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        items(performanceMetrics) { metric ->
                            PerformanceMetricCard(metric)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SummaryCard(
    summary: ReportSummary,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(140.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
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
                        .background(summary.color.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = summary.icon,
                        contentDescription = null,
                        tint = summary.color,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Column {
                Text(
                    text = summary.value,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = summary.title,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = summary.change,
                    style = MaterialTheme.typography.labelSmall,
                    color = if (summary.isPositive) Color(0xFF10B981) else Color(0xFFEF4444),
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
private fun CampaignReportCard(campaign: CampaignReport) {
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
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = campaign.campaignName,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${campaign.startDate} - ${campaign.endDate}",
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
                        containerColor = if (campaign.status == "Tamamlandı")
                            Color(0xFF10B981).copy(alpha = 0.1f)
                        else
                            Color(0xFF3B82F6).copy(alpha = 0.1f),
                        labelColor = if (campaign.status == "Tamamlandı")
                            Color(0xFF10B981)
                        else
                            Color(0xFF3B82F6)
                    )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ReportMetricSmall("Erişim", campaign.totalReach, Icons.Default.Visibility)
                ReportMetricSmall("Etkileşim", campaign.engagement, Icons.Default.TrendingUp)
                ReportMetricSmall("ROI", campaign.roi, Icons.Default.ShowChart)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Divider()

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Harcama",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = campaign.spent,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.People,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${campaign.influencersCount} İnfluencer",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                FilledTonalButton(
                    onClick = { },
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Detaylar")
                }
            }
        }
    }
}

@Composable
private fun ReportMetricSmall(
    label: String,
    value: String,
    icon: ImageVector
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
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
private fun PerformanceMetricCard(metric: PerformanceMetric) {
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
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = metric.label,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Önceki: ${metric.previous}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = metric.current,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (metric.isPositive) Icons.Default.ArrowUpward else Icons.Default.ArrowDownward,
                        contentDescription = null,
                        tint = if (metric.isPositive) Color(0xFF10B981) else Color(0xFFEF4444),
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = metric.change,
                        style = MaterialTheme.typography.labelMedium,
                        color = if (metric.isPositive) Color(0xFF10B981) else Color(0xFFEF4444),
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Composable
private fun CampaignDistributionCard() {
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
            Text(
                text = "Kategori Bazında Kampanyalar",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            CategoryDistributionItem("Teknoloji", 35, Color(0xFF6366F1))
            CategoryDistributionItem("Moda", 25, Color(0xFF8B5CF6))
            CategoryDistributionItem("Spor", 20, Color(0xFF10B981))
            CategoryDistributionItem("Diğer", 20, Color(0xFFF59E0B))
        }
    }
}

@Composable
private fun CategoryDistributionItem(
    category: String,
    percentage: Int,
    color: Color
) {
    Column(modifier = Modifier.padding(vertical = 6.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = category,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "$percentage%",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = color
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = percentage / 100f,
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = color,
            trackColor = color.copy(alpha = 0.2f)
        )
    }
}

@Composable
private fun PlatformAnalysisCard() {
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
            Text(
                text = "Platform Performansı",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            PlatformAnalysisItem("Instagram", "1.2M", "7.2%", Color(0xFFE4405F))
            PlatformAnalysisItem("YouTube", "850K", "6.5%", Color(0xFFFF0000))
            PlatformAnalysisItem("TikTok", "520K", "9.1%", Color(0xFF000000))
        }
    }
}

@Composable
private fun PlatformAnalysisItem(
    platform: String,
    reach: String,
    engagement: String,
    color: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .clip(CircleShape)
                    .background(color)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = platform,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = reach,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Erişim",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = engagement,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF10B981)
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
