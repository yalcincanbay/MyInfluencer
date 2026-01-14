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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mustafa.influencer.domain.model.Campaign

// Süsleme için veri modelleri
data class CampaignStat(val title: String, val value: String, val icon: ImageVector, val color: Color, val subtitle: String = "")
data class InfluencerCard(val name: String, val username: String, val platform: String, val followers: String, val engagement: String, val category: String, val priceRange: String, val isVerified: Boolean = false, val profileColor: Color)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdvertiserHomeScreen(
    onInfluencerSearchClick: () -> Unit = {},
    onCreateCampaignClick: () -> Unit = {},
    onReportsClick: () -> Unit = {},
    vm: AdvertiserViewModel = viewModel()
) {
    val uiState by vm.state.collectAsState()

    LaunchedEffect(Unit) {
        vm.loadMyCampaigns()
    }

    val campaignStats = listOf(
        CampaignStat("Aktif Kampanya", uiState.myCampaigns.size.toString(), Icons.Default.Campaign, Color(0xFF6366F1), "Güncel"),
        CampaignStat("Toplam Bütçe", uiState.totalBudget, Icons.Default.AccountBalanceWallet, Color(0xFF10B981), "Planlanan")
    )

    val recommendedInfluencers = listOf(
        InfluencerCard("Ayşe Yılmaz", "@ayse_tech", "YouTube", "125K", "6.8%", "Teknoloji", "₺5k-12k", true, Color(0xFFFF6B6B)),
        InfluencerCard("Mehmet Kaya", "@mehmet_game", "Twitch", "89K", "8.2%", "Oyun", "₺8k-15k", true, Color(0xFF4ECDC4))
    )

    LazyColumn(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        item { AdvertiserWelcomeHeader() }
        item { CampaignStatsSection(campaignStats) }
        item {
            AdvertiserQuickActions(
                onInfluencerSearchClick = onInfluencerSearchClick,
                onCreateCampaignClick = onCreateCampaignClick,
                onReportsClick = onReportsClick
            )
        }

        if (uiState.isLoading) {
            item { Box(Modifier.fillMaxWidth().padding(20.dp), contentAlignment = Alignment.Center) { CircularProgressIndicator() } }
        } else if (uiState.myCampaigns.isNotEmpty()) {
            item { AdSectionHeader(title = "Aktif Kampanyalarım", subtitle = "${uiState.myCampaigns.size} kampanya listeleniyor", icon = Icons.Default.Rocket) }
            item { ActiveCampaignsAdSection(uiState.myCampaigns) }
        } else {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth().padding(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(Modifier.padding(20.dp).fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Henüz aktif kampandanız yok.")
                        Spacer(Modifier.height(8.dp))
                        Button(onClick = onCreateCampaignClick) { Text("Hemen Oluştur") }
                    }
                }
            }
        }

        item { AdSectionHeader(title = "Önerilen İçerik Üreticiler", subtitle = "Profilinize uygun", icon = Icons.Default.Stars) }
        item { RecommendedInfluencersSection(recommendedInfluencers) }
    }
}

@Composable
private fun AdvertiserWelcomeHeader() {
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 16.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Box(
            modifier = Modifier.fillMaxWidth().background(Brush.horizontalGradient(colors = listOf(Color(0xFF6366F1), Color(0xFF8B5CF6)))).padding(24.dp)
        ) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Kampanyalarınız Aktif 🚀", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = Color.White)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Yeni başvurular ve güncellemeler sizi bekliyor", style = MaterialTheme.typography.bodyMedium, color = Color.White.copy(alpha = 0.9f))
                }
            }
        }
    }
}

@Composable
private fun CampaignStatsSection(stats: List<CampaignStat>) {
    LazyRow(modifier = Modifier.fillMaxWidth(), contentPadding = PaddingValues(horizontal = 20.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        items(stats) { stat -> CampaignStatCard(stat) }
    }
    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
private fun CampaignStatCard(stat: CampaignStat) {
    Card(
        modifier = Modifier.width(180.dp).height(130.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.SpaceBetween) {
            Icon(imageVector = stat.icon, contentDescription = null, tint = stat.color, modifier = Modifier.size(24.dp))
            Column {
                Text(stat.value, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                Text(stat.title, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@Composable
private fun AdvertiserQuickActions(onInfluencerSearchClick: () -> Unit, onCreateCampaignClick: () -> Unit, onReportsClick: () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 16.dp)) {
        Text("Hızlı İşlemler", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 12.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            AdQuickActionCard("Kampanya Oluştur", Icons.Default.Add, Color(0xFF6366F1), onCreateCampaignClick, Modifier.weight(1f))
            AdQuickActionCard("İnfluencer Ara", Icons.Default.Search, Color(0xFF8B5CF6), onInfluencerSearchClick, Modifier.weight(1f))
            AdQuickActionCard("Raporlar", Icons.Default.Assessment, Color(0xFF10B981), onReportsClick, Modifier.weight(1f))
        }
    }
}

@Composable
private fun AdQuickActionCard(title: String, icon: ImageVector, color: Color, onClick: () -> Unit, modifier: Modifier) {
    Card(modifier = modifier.height(95.dp), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.1f)), onClick = onClick) {
        Column(modifier = Modifier.fillMaxSize().padding(12.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(32.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(title, style = MaterialTheme.typography.labelMedium, color = color, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
private fun AdSectionHeader(title: String, subtitle: String, icon: ImageVector) {
    Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 16.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(imageVector = icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Text(subtitle, style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
private fun ActiveCampaignsAdSection(campaigns: List<Campaign>) {
    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        campaigns.forEach { campaign -> ActiveCampaignAdCard(campaign) }
    }
    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
private fun ActiveCampaignAdCard(campaign: Campaign) {
    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(campaign.title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Text(campaign.deadlineText.ifBlank { "Süresiz" }, style = MaterialTheme.typography.bodySmall)
                }
                AssistChip(onClick = {}, label = { Text(campaign.status) })
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                CampaignMetricItem(Icons.Default.Category, "Kategori", campaign.category, Color(0xFF6366F1))
                CampaignMetricItem(Icons.Default.Public, "Platform", campaign.platform, Color(0xFF8B5CF6))
                CampaignMetricItem(Icons.Default.Payments, "Bütçe", "₺${campaign.budget}", Color(0xFFF59E0B))
            }
        }
    }
}

@Composable
private fun CampaignMetricItem(icon: ImageVector, label: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(20.dp))
        Text(value, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
        Text(label, style = MaterialTheme.typography.labelSmall)
    }
}

@Composable
private fun RecommendedInfluencersSection(influencers: List<InfluencerCard>) {
    LazyRow(modifier = Modifier.fillMaxWidth(), contentPadding = PaddingValues(horizontal = 20.dp), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        items(influencers) { influencer -> RecommendedInfluencerCard(influencer) }
    }
    Spacer(modifier = Modifier.height(16.dp))
}

@Composable
private fun RecommendedInfluencerCard(influencer: InfluencerCard) {
    Card(modifier = Modifier.width(280.dp).height(200.dp), shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
        Column(modifier = Modifier.fillMaxSize().padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(56.dp).clip(CircleShape).background(influencer.profileColor))
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(influencer.name, fontWeight = FontWeight.Bold)
                    Text(influencer.username, style = MaterialTheme.typography.bodySmall)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column { Text("Takipçi", style = MaterialTheme.typography.labelSmall); Text(influencer.followers, fontWeight = FontWeight.Bold) }
                Column(horizontalAlignment = Alignment.End) { Text("Fiyat", style = MaterialTheme.typography.labelSmall); Text(influencer.priceRange, fontWeight = FontWeight.Bold, color = Color(0xFF10B981)) }
            }
        }
    }
}