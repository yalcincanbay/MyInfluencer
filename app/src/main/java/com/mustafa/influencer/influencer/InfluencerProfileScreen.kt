package com.mustafa.influencer.influencer

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

// Mock Data Models for Profile Stats
data class ProfileStat(
    val label: String,
    val value: String,
    val icon: ImageVector,
    val color: Color
)

data class AchievementBadge(
    val title: String,
    val description: String,
    val icon: ImageVector,
    val isUnlocked: Boolean
)

data class PlatformDetail(
    val name: String,
    val icon: ImageVector,
    val followers: String,
    val engagement: String,
    val link: String?,
    val color: Color
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InfluencerProfileScreen(
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
    val profileStats = listOf(
        ProfileStat(
            label = "Takipçi",
            value = "24.5K",
            icon = Icons.Default.People,
            color = Color(0xFF6366F1)
        ),
        ProfileStat(
            label = "Etkileşim",
            value = "8.2%",
            icon = Icons.Default.TrendingUp,
            color = Color(0xFF8B5CF6)
        ),
        ProfileStat(
            label = "Kampanya",
            value = "12",
            icon = Icons.Default.CheckCircle,
            color = Color(0xFF10B981)
        ),
        ProfileStat(
            label = "Puan",
            value = "4.8",
            icon = Icons.Default.Star,
            color = Color(0xFFF59E0B)
        )
    )

    val achievements = listOf(
        AchievementBadge(
            title = "İlk Kampanya",
            description = "İlk kampanyanı tamamladın",
            icon = Icons.Default.EmojiEvents,
            isUnlocked = true
        ),
        AchievementBadge(
            title = "Süper İnfluencer",
            description = "10+ başarılı kampanya",
            icon = Icons.Default.WorkspacePremium,
            isUnlocked = true
        ),
        AchievementBadge(
            title = "Hız Rekoru",
            description = "24 saat içinde kampanya tamamla",
            icon = Icons.Default.Bolt,
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
            // Profile Header with Cover
            item {
                ProfileHeader(
                    email = userData?.email ?: "",
                    onEditProfile = { /* TODO */ }
                )
            }

            // Stats Overview
            item {
                StatsOverview(profileStats)
            }

            // Quick Actions
            item {
                ProfileQuickActions()
            }

            // Platforms Section
            if (userData?.platforms?.isNotEmpty() == true) {
                item {
                    SectionTitle(
                        title = "Sosyal Medya Hesaplarım",
                        icon = Icons.Default.Share
                    )
                }
                item {
                    PlatformsSection(
                        platforms = userData?.platforms ?: emptyList(),
                        platformLinks = userData?.platformLinks ?: emptyMap()
                    )
                }
            }

            // Categories Section
            if (userData?.categories?.isNotEmpty() == true) {
                item {
                    SectionTitle(
                        title = "İçerik Kategorilerim",
                        icon = Icons.Default.Category
                    )
                }
                item {
                    CategoriesSection(categories = userData?.categories ?: emptyList())
                }
            }

            // Bio Section
            if (userData?.bio?.isNotBlank() == true) {
                item {
                    SectionTitle(
                        title = "Hakkımda",
                        icon = Icons.Default.Person
                    )
                }
                item {
                    BioSection(bio = userData?.bio ?: "")
                }
            }

            // Achievements Section
            item {
                SectionTitle(
                    title = "Başarılarım",
                    icon = Icons.Default.MilitaryTech
                )
            }
            item {
                AchievementsSection(achievements)
            }

            // Settings & Logout Section
            item {
                SettingsSection(onLogout = onLogout)
            }
        }
    }
}

@Composable
private fun ProfileHeader(
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
                            Color(0xFFEC4899)
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
            // Profile Picture
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
                    imageVector = Icons.Default.Person,
                    contentDescription = "Profil",
                    modifier = Modifier.size(60.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Email
            Text(
                text = email,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = "İçerik Üreticisi",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

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
private fun StatsOverview(stats: List<ProfileStat>) {
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
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun ProfileQuickActions() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .offset(y = (-20).dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        QuickActionButton(
            icon = Icons.Default.BarChart,
            label = "İstatistikler",
            color = Color(0xFF6366F1),
            modifier = Modifier.weight(1f)
        )
        QuickActionButton(
            icon = Icons.Default.Campaign,
            label = "Kampanyalarım",
            color = Color(0xFF8B5CF6),
            modifier = Modifier.weight(1f)
        )
        QuickActionButton(
            icon = Icons.Default.Wallet,
            label = "Kazançlarım",
            color = Color(0xFF10B981),
            modifier = Modifier.weight(1f)
        )
    }
    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
private fun QuickActionButton(
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
private fun SectionTitle(
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
private fun PlatformsSection(
    platforms: List<String>,
    platformLinks: Map<String, String?>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        platforms.forEach { platform ->
            val platformInfo = getPlatformInfo(platform)
            PlatformCard(
                platform = platformInfo,
                link = platformLinks[platform]
            )
        }
    }
    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
private fun PlatformCard(
    platform: PlatformDetail,
    link: String?
) {
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
                if (!link.isNullOrBlank()) {
                    Text(
                        text = link,
                        style = MaterialTheme.typography.bodySmall,
                        color = platform.color,
                        maxLines = 1
                    )
                } else {
                    Text(
                        text = "${platform.followers} takipçi • ${platform.engagement} etkileşim",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
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
private fun CategoriesSection(categories: List<String>) {
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
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    labelColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    leadingIconContentColor = MaterialTheme.colorScheme.onSecondaryContainer
                ),
                shape = RoundedCornerShape(12.dp)
            )
        }
    }
    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
private fun BioSection(bio: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Text(
            text = bio,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(20.dp),
            lineHeight = MaterialTheme.typography.bodyLarge.lineHeight
        )
    }
    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
private fun AchievementsSection(achievements: List<AchievementBadge>) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(achievements) { achievement ->
            AchievementCard(achievement)
        }
    }
    Spacer(modifier = Modifier.height(16.dp))
}

@Composable
private fun AchievementCard(achievement: AchievementBadge) {
    Card(
        modifier = Modifier
            .width(160.dp)
            .height(180.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (achievement.isUnlocked)
                MaterialTheme.colorScheme.tertiaryContainer
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
                    MaterialTheme.colorScheme.onTertiaryContainer
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
private fun SettingsSection(onLogout: () -> Unit) {
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

        SettingsItem(
            icon = Icons.Outlined.Notifications,
            title = "Bildirimler",
            subtitle = "Kampanya ve mesaj bildirimleri"
        )

        SettingsItem(
            icon = Icons.Outlined.Security,
            title = "Gizlilik",
            subtitle = "Hesap güvenliği ve gizlilik"
        )

        SettingsItem(
            icon = Icons.Outlined.Help,
            title = "Yardım & Destek",
            subtitle = "SSS ve destek merkezi"
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
private fun SettingsItem(
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

// Helper function to get platform info (mock data)
private fun getPlatformInfo(platform: String): PlatformDetail {
    return when (platform.lowercase()) {
        "youtube" -> PlatformDetail(
            name = "YouTube",
            icon = Icons.Default.VideoLibrary,
            followers = "12.5K",
            engagement = "5.2%",
            link = null,
            color = Color(0xFFFF0000)
        )
        "instagram" -> PlatformDetail(
            name = "Instagram",
            icon = Icons.Default.PhotoLibrary,
            followers = "8.3K",
            engagement = "6.8%",
            link = null,
            color = Color(0xFFE1306C)
        )
        "tiktok" -> PlatformDetail(
            name = "TikTok",
            icon = Icons.Default.MusicNote,
            followers = "15.2K",
            engagement = "9.4%",
            link = null,
            color = Color(0xFF000000)
        )
        "twitter" -> PlatformDetail(
            name = "Twitter",
            icon = Icons.Default.Facebook,
            followers = "5.8K",
            engagement = "3.2%",
            link = null,
            color = Color(0xFF1DA1F2)
        )
        "facebook" -> PlatformDetail(
            name = "Facebook",
            icon = Icons.Default.Facebook,
            followers = "3.4K",
            engagement = "2.1%",
            link = null,
            color = Color(0xFF1877F2)
        )
        else -> PlatformDetail(
            name = platform.replaceFirstChar { it.uppercase() },
            icon = Icons.Default.Public,
            followers = "0",
            engagement = "0%",
            link = null,
            color = Color(0xFF6366F1)
        )
    }
}
