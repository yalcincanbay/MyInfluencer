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
import com.mustafa.influencer.shared.FirebaseManager
import kotlinx.coroutines.launch

// Mock Data Models for Advertiser Profile
data class CompanyStat(
    val label: String,
    val value: String,
    val icon: ImageVector,
    val color: Color
)

data class PlatformPreference(
    val name: String,
    val icon: ImageVector,
    val color: Color,
    val campaignCount: Int
)

data class CompanyAchievement(
    val title: String,
    val description: String,
    val icon: ImageVector,
    val isUnlocked: Boolean
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdvertiserProfileScreen(
    onLogout: () -> Unit
) {
    var userData by remember { mutableStateOf<com.mustafa.influencer.data.User?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()

    // Kullanıcı verilerini yükle
    LaunchedEffect(Unit) {
        scope.launch {
            val userId = FirebaseManager.getCurrentUserId()
            if (userId != null) {
                val result = FirebaseManager.getUserData(userId)
                result.onSuccess { user ->
                    userData = user
                }
            }
            isLoading = false
        }
    }

    // Mock Stats
    val companyStats = listOf(
        CompanyStat(
            label = "Kampanya",
            value = "12",
            icon = Icons.Default.Campaign,
            color = Color(0xFF6366F1)
        ),
        CompanyStat(
            label = "İnfluencer",
            value = "28",
            icon = Icons.Default.People,
            color = Color(0xFF8B5CF6)
        ),
        CompanyStat(
            label = "Toplam Erişim",
            value = "2.4M",
            icon = Icons.Default.Visibility,
            color = Color(0xFF10B981)
        ),
        CompanyStat(
            label = "Başarı Oranı",
            value = "94%",
            icon = Icons.Default.TrendingUp,
            color = Color(0xFFF59E0B)
        )
    )

    val achievements = listOf(
        CompanyAchievement(
            title = "İlk Kampanya",
            description = "İlk kampanyanı başlattın",
            icon = Icons.Default.EmojiEvents,
            isUnlocked = true
        ),
        CompanyAchievement(
            title = "Güvenilir Marka",
            description = "10+ başarılı işbirliği",
            icon = Icons.Default.Shield,
            isUnlocked = true
        ),
        CompanyAchievement(
            title = "Büyük Yatırımcı",
            description = "₺100K+ toplam harcama",
            icon = Icons.Default.Diamond,
            isUnlocked = false
        )
    )

    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            // Company Header with Cover
            item {
                CompanyProfileHeader(
                    companyName = userData?.companyName ?: "",
                    email = userData?.email ?: "",
                    onEditProfile = { /* TODO */ }
                )
            }

            // Stats Overview
            item {
                CompanyStatsOverview(companyStats)
            }

            // Quick Actions
            item {
                CompanyQuickActions()
            }

            // Platform Preferences Section
            if (userData?.platforms?.isNotEmpty() == true) {
                item {
                    AdProfileSectionTitle(
                        title = "Reklam Verdiğim Platformlar",
                        icon = Icons.Default.Devices
                    )
                }
                item {
                    PlatformPreferencesSection(
                        platforms = userData?.platforms ?: emptyList()
                    )
                }
            }

            // Target Categories Section
            if (userData?.categories?.isNotEmpty() == true) {
                item {
                    AdProfileSectionTitle(
                        title = "İlgilendiğim Alanlar",
                        icon = Icons.Default.Category
                    )
                }
                item {
                    TargetCategoriesSection(categories = userData?.categories ?: emptyList())
                }
            }

            // Company Info Section
            item {
                AdProfileSectionTitle(
                    title = "Şirket Bilgileri",
                    icon = Icons.Default.Business
                )
            }
            item {
                CompanyInfoSection(
                    companyName = userData?.companyName ?: "",
                    email = userData?.email ?: ""
                )
            }

            // Achievements Section
            item {
                AdProfileSectionTitle(
                    title = "Başarılar & Rozetler",
                    icon = Icons.Default.MilitaryTech
                )
            }
            item {
                CompanyAchievementsSection(achievements)
            }

            // Settings & Logout Section
            item {
                CompanySettingsSection(onLogout = onLogout)
            }
        }
    }
}

@Composable
private fun CompanyProfileHeader(
    companyName: String,
    email: String,
    onEditProfile: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        // Cover Background
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF6366F1),
                            Color(0xFF8B5CF6),
                            Color(0xFF10B981)
                        )
                    )
                )
        )

        // Profile Content
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = (-60).dp)
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Company Logo
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .border(
                        width = 4.dp,
                        color = MaterialTheme.colorScheme.background,
                        shape = CircleShape
                    )
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surface),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Business,
                    contentDescription = "Şirket",
                    modifier = Modifier.size(60.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Company Name
            Text(
                text = companyName,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )

            Text(
                text = "Reklam Veren",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Verified,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                    tint = Color(0xFF10B981)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Onaylı Şirket",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF10B981),
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Edit Profile Button
            FilledTonalButton(
                onClick = onEditProfile,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.height(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Profili Düzenle")
            }
        }
    }
}

@Composable
private fun CompanyStatsOverview(stats: List<CompanyStat>) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .offset(y = (-40).dp),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            stats.forEach { stat ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
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
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stat.value,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = stat.label,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
private fun CompanyQuickActions() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .offset(y = (-20).dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        CompanyQuickActionButton(
            icon = Icons.Default.Campaign,
            label = "Kampanyalarım",
            color = Color(0xFF6366F1),
            modifier = Modifier.weight(1f)
        )
        CompanyQuickActionButton(
            icon = Icons.Default.AccountBalanceWallet,
            label = "Bütçe",
            color = Color(0xFF8B5CF6),
            modifier = Modifier.weight(1f)
        )
        CompanyQuickActionButton(
            icon = Icons.Default.Assessment,
            label = "Raporlar",
            color = Color(0xFF10B981),
            modifier = Modifier.weight(1f)
        )
    }
    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
private fun CompanyQuickActionButton(
    icon: ImageVector,
    label: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(100.dp),
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
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = color,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun AdProfileSectionTitle(
    title: String,
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
                .size(36.dp)
                .clip(RoundedCornerShape(10.dp))
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
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun PlatformPreferencesSection(platforms: List<String>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        platforms.forEach { platform ->
            val platformInfo = getAdvertiserPlatformInfo(platform)
            PlatformPreferenceCard(platform = platformInfo)
        }
    }
    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
private fun PlatformPreferenceCard(platform: PlatformPreference) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(platform.color.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = platform.icon,
                    contentDescription = null,
                    tint = platform.color,
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = platform.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "${platform.campaignCount} kampanya",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun TargetCategoriesSection(categories: List<String>) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(categories) { category ->
            AssistChip(
                onClick = { },
                label = {
                    Text(
                        text = category.replaceFirstChar { it.uppercase() },
                        fontWeight = FontWeight.Medium
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Tag,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                },
                colors = AssistChipDefaults.assistChipColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                    labelColor = MaterialTheme.colorScheme.onTertiaryContainer,
                    leadingIconContentColor = MaterialTheme.colorScheme.onTertiaryContainer
                ),
                shape = RoundedCornerShape(12.dp)
            )
        }
    }
    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
private fun CompanyInfoSection(
    companyName: String,
    email: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CompanyInfoItem(
                icon = Icons.Default.Business,
                label = "Şirket Adı",
                value = companyName
            )
            Divider()
            CompanyInfoItem(
                icon = Icons.Default.Email,
                label = "E-posta",
                value = email
            )
            Divider()
            CompanyInfoItem(
                icon = Icons.Default.CalendarMonth,
                label = "Üyelik Tarihi",
                value = "Ocak 2024"
            )
        }
    }
    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
private fun CompanyInfoItem(
    icon: ImageVector,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
private fun CompanyAchievementsSection(achievements: List<CompanyAchievement>) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(achievements) { achievement ->
            CompanyAchievementCard(achievement)
        }
    }
    Spacer(modifier = Modifier.height(16.dp))
}

@Composable
private fun CompanyAchievementCard(achievement: CompanyAchievement) {
    Card(
        modifier = Modifier
            .width(160.dp)
            .height(180.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (achievement.isUnlocked)
                Color(0xFF10B981).copy(alpha = 0.1f)
            else
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(
                        if (achievement.isUnlocked)
                            Color(0xFFF59E0B).copy(alpha = 0.2f)
                        else
                            MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.1f)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = achievement.icon,
                    contentDescription = null,
                    tint = if (achievement.isUnlocked) Color(0xFFF59E0B) else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(32.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = achievement.title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = if (achievement.isUnlocked)
                    MaterialTheme.colorScheme.onSurface
                else
                    MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = achievement.description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                maxLines = 2
            )
        }
    }
}

@Composable
private fun CompanySettingsSection(onLogout: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Ayarlar",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        CompanySettingsItem(
            icon = Icons.Outlined.Payments,
            title = "Ödeme Yöntemleri",
            subtitle = "Kredi kartı ve fatura ayarları"
        )

        CompanySettingsItem(
            icon = Icons.Outlined.Notifications,
            title = "Bildirimler",
            subtitle = "Başvuru ve kampanya bildirimleri"
        )

        CompanySettingsItem(
            icon = Icons.Outlined.Security,
            title = "Güvenlik",
            subtitle = "Şifre ve hesap güvenliği"
        )

        CompanySettingsItem(
            icon = Icons.Outlined.Help,
            title = "Yardım & Destek",
            subtitle = "İletişim ve destek merkezi"
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Logout Button
        FilledTonalButton(
            onClick = onLogout,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.filledTonalButtonColors(
                containerColor = MaterialTheme.colorScheme.errorContainer,
                contentColor = MaterialTheme.colorScheme.onErrorContainer
            )
        ) {
            Icon(
                imageVector = Icons.Default.Logout,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Çıkış Yap",
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun CompanySettingsItem(
    icon: ImageVector,
    title: String,
    subtitle: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

// Helper function to get platform info for advertisers (mock data)
private fun getAdvertiserPlatformInfo(platform: String): PlatformPreference {
    return when (platform.lowercase()) {
        "youtube" -> PlatformPreference(
            name = "YouTube",
            icon = Icons.Default.VideoLibrary,
            color = Color(0xFFFF0000),
            campaignCount = 5
        )
        "instagram" -> PlatformPreference(
            name = "Instagram",
            icon = Icons.Default.PhotoLibrary,
            color = Color(0xFFE1306C),
            campaignCount = 8
        )
        "tiktok" -> PlatformPreference(
            name = "TikTok",
            icon = Icons.Default.MusicNote,
            color = Color(0xFF000000),
            campaignCount = 3
        )
        "twitter" -> PlatformPreference(
            name = "Twitter",
            icon = Icons.Default.Favorite,
            color = Color(0xFF1DA1F2),
            campaignCount = 2
        )
        "facebook" -> PlatformPreference(
            name = "Facebook",
            icon = Icons.Default.Facebook,
            color = Color(0xFF1877F2),
            campaignCount = 4
        )
        else -> PlatformPreference(
            name = platform.replaceFirstChar { it.uppercase() },
            icon = Icons.Default.Public,
            color = Color(0xFF6366F1),
            campaignCount = 0
        )
    }
}
