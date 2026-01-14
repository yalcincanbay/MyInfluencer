package com.mustafa.influencer.advertiser

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mustafa.influencer.domain.model.Campaign
import com.mustafa.influencer.shared.FirebaseManager
import kotlinx.coroutines.launch

// Veri Modeli
data class CompanyStat(val label: String, val value: String, val icon: ImageVector, val color: Color)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdvertiserProfileScreen(
    onLogout: () -> Unit,
    vm: AdvertiserViewModel = viewModel()
) {
    var userData by remember { mutableStateOf<com.mustafa.influencer.data.User?>(null) }
    val scope = rememberCoroutineScope()
    val uiState by vm.state.collectAsState()

    LaunchedEffect(Unit) {
        vm.loadMyCampaigns()
        scope.launch {
            val userId = FirebaseManager.getCurrentUserId()
            if (userId != null) {
                val result = FirebaseManager.getUserData(userId)
                result.onSuccess { user -> userData = user }
            }
        }
    }

    val companyStats = listOf(
        CompanyStat("Kampanya", uiState.myCampaigns.size.toString(), Icons.Default.Campaign, Color(0xFF6366F1)),
        CompanyStat("Bütçe", uiState.totalBudget, Icons.Default.AccountBalanceWallet, Color(0xFF8B5CF6)),
        CompanyStat("Başarı", "94%", Icons.Default.TrendingUp, Color(0xFFF59E0B))
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        // 1. Üst Başlık (Header)
        item {
            CompanyProfileHeader(
                companyName = userData?.companyName ?: "Yükleniyor...",
                email = userData?.email ?: "",
                onEditProfile = {}
            )
        }

        // 2. İstatistikler (Yukarı kaydırılmış görünüm)
        item { CompanyStatsOverview(companyStats) }

        // Görsel Düzeltme: İstatistik kartı yukarı kaydığı (-40dp) için,
        // altındaki elemanlar çok yukarı çıkmasın diye boşluk bırakıyoruz.
        item { Spacer(modifier = Modifier.height(16.dp)) }

        // 3. Kampanyalarım Listesi (Varsa Göster)
        if (uiState.myCampaigns.isNotEmpty()) {
            item { AdProfileSectionTitle(title = "Kampanyalarım", icon = Icons.Default.List) }
            item {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(uiState.myCampaigns) { campaign -> SmallCampaignCard(campaign) }
                }
            }
            // Kampanyalar ile alttaki butonlar arasına boşluk
            item { Spacer(modifier = Modifier.height(24.dp)) }
        } else {
            // Kampanya yoksa da biraz boşluk bırakalım ki butonlar istatistiklere yapışmasın
            item { Spacer(modifier = Modifier.height(16.dp)) }
        }

        // 4. Hızlı İşlemler (Artık yukarı kaymıyor, düz duruyor)
        item { CompanyQuickActions() }

        // 5. Ayarlar ve Çıkış
        item { CompanySettingsSection(onLogout = onLogout) }
    }
}

@Composable
fun SmallCampaignCard(campaign: Campaign) {
    Card(
        modifier = Modifier
            .width(200.dp)
            .height(120.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
    ) {
        Column(Modifier.padding(12.dp)) {
            Text(campaign.title, fontWeight = FontWeight.Bold, maxLines = 1)
            Spacer(Modifier.height(4.dp))
            Text(campaign.category, style = MaterialTheme.typography.bodySmall)
            Spacer(Modifier.weight(1f))
            Text("₺${campaign.budget}", color = Color(0xFF10B981), fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun CompanyProfileHeader(companyName: String, email: String, onEditProfile: () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .background(Brush.linearGradient(colors = listOf(Color(0xFF6366F1), Color(0xFF8B5CF6), Color(0xFF10B981))))
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = (-60).dp)
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .border(4.dp, MaterialTheme.colorScheme.background, CircleShape)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surface),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Business, null, modifier = Modifier.size(60.dp), tint = MaterialTheme.colorScheme.primary)
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(companyName, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Text("Reklam Veren", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(8.dp))
            FilledTonalButton(onClick = onEditProfile) { Text("Profili Düzenle") }
        }
    }
}

@Composable
private fun CompanyStatsOverview(stats: List<CompanyStat>) {
    // Bu kart yukarı (-40dp) kaymaya devam ediyor, görsel estetik için.
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .offset(y = (-40).dp),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            stats.forEach { stat ->
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                    Icon(stat.icon, null, tint = stat.color)
                    Text(stat.value, fontWeight = FontWeight.Bold)
                    Text(stat.label, style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}

@Composable
private fun CompanyQuickActions() {
    // DÜZELTME: Buradaki .offset(y = (-20).dp) kaldırıldı.
    // Artık bu bölüm kampanyaların veya istatistiklerin altına düzgünce yerleşecek.
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp), // offset yerine sadece padding
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        CompanyQuickActionButton(Icons.Default.Campaign, "Kampanyalar", Color(0xFF6366F1), Modifier.weight(1f))
        CompanyQuickActionButton(Icons.Default.AccountBalanceWallet, "Bütçe", Color(0xFF8B5CF6), Modifier.weight(1f))
        CompanyQuickActionButton(Icons.Default.Assessment, "Raporlar", Color(0xFF10B981), Modifier.weight(1f))
    }
}

@Composable
private fun CompanyQuickActionButton(icon: ImageVector, label: String, color: Color, modifier: Modifier) {
    Card(
        modifier = modifier.height(100.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.1f))
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(icon, null, tint = color)
            Text(label, color = color, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
private fun AdProfileSectionTitle(title: String, icon: ImageVector) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, null, tint = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.width(12.dp))
        Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun CompanySettingsSection(onLogout: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 20.dp), // Biraz üst boşluk eklendi
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Ayarlar", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        CompanySettingsItem(Icons.Outlined.Payments, "Ödeme", "Fatura ayarları")
        CompanySettingsItem(Icons.Outlined.Notifications, "Bildirimler", "Uygulama bildirimleri")

        Spacer(Modifier.height(8.dp))

        FilledTonalButton(
            onClick = onLogout,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.filledTonalButtonColors(
                containerColor = MaterialTheme.colorScheme.errorContainer,
                contentColor = MaterialTheme.colorScheme.onErrorContainer
            )
        ) {
            Icon(Icons.Default.Logout, null)
            Spacer(Modifier.width(8.dp))
            Text("Çıkış Yap")
        }
    }
}

@Composable
private fun CompanySettingsItem(icon: ImageVector, title: String, subtitle: String) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, null, tint = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(title, fontWeight = FontWeight.SemiBold)
                Text(subtitle, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}