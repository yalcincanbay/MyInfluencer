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
data class CampaignDetail(
    val title: String,
    val advertiser: String,
    val status: String,
    val description: String,
    val budget: String,
    val deadline: String,
    val platforms: List<String>,
    val requirements: List<String>,
    val deliverables: List<String>,
    val progress: Float
)

data class Milestone(
    val title: String,
    val date: String,
    val isCompleted: Boolean,
    val description: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CampaignDetailScreen(
    campaignId: String,
    onBackClick: () -> Unit,
    onMessageClick: () -> Unit = {}
) {
    // Mock Campaign Detail
    val campaign = CampaignDetail(
        title = "Yeni Telefon İncelemesi",
        advertiser = "TechVision A.Ş.",
        status = "Devam Ediyor",
        description = "Yeni flagship telefonumuz için detaylı inceleme videosu. Ürünün kamera özellikleri, performansı ve kullanım deneyimini kapsamlı bir şekilde ele alan içerik bekliyoruz. Video en az 10 dakika uzunluğunda olmalı ve organik bir şekilde ürünü tanıtmalıdır.",
        budget = "₺12,000",
        deadline = "5 gün kaldı",
        platforms = listOf("YouTube", "Instagram"),
        requirements = listOf(
            "Minimum 10K takipçi",
            "Teknoloji içeriği üreticisi",
            "Daha önce telefon incelemesi yaptı",
            "Ortalama %5+ engagement oranı"
        ),
        deliverables = listOf(
            "1 adet YouTube inceleme videosu (10+ dakika)",
            "3 adet Instagram Reels",
            "5 adet Instagram Story",
            "Kampanya hashtag'lerini kullanma"
        ),
        progress = 0.65f
    )

    val milestones = listOf(
        Milestone(
            title = "Kampanya Anlaşması",
            date = "5 Ocak 2025",
            isCompleted = true,
            description = "Sözleşme imzalandı ve kampanya başlatıldı"
        ),
        Milestone(
            title = "Ürün Teslimatı",
            date = "8 Ocak 2025",
            isCompleted = true,
            description = "İnceleme ürünü teslim edildi"
        ),
        Milestone(
            title = "İçerik Onayı",
            date = "15 Ocak 2025",
            isCompleted = true,
            description = "Taslak içerik onaylandı"
        ),
        Milestone(
            title = "Yayınlama",
            date = "18 Ocak 2025",
            isCompleted = false,
            description = "İçeriğin yayınlanması bekleniyor"
        ),
        Milestone(
            title = "Ödeme",
            date = "25 Ocak 2025",
            isCompleted = false,
            description = "Kampanya ödemesi"
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Kampanya Detayı") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Geri")
                    }
                },
                actions = {
                    IconButton(onClick = { /* Share campaign */ }) {
                        Icon(Icons.Default.Share, contentDescription = "Paylaş")
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
                CampaignHeaderCard(campaign)
            }

            // Progress Section
            item {
                ProgressSection(campaign.progress)
            }

            // Quick Actions
            item {
                QuickActionsRow(onMessageClick)
            }

            // Description
            item {
                DetailSection(
                    title = "Kampanya Açıklaması",
                    icon = Icons.Default.Description
                ) {
                    Text(
                        text = campaign.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Requirements
            item {
                DetailSection(
                    title = "Gereksinimler",
                    icon = Icons.Default.CheckCircle
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        campaign.requirements.forEach { requirement ->
                            RequirementItem(requirement)
                        }
                    }
                }
            }

            // Deliverables
            item {
                DetailSection(
                    title = "Teslim Edilecekler",
                    icon = Icons.Default.Assignment
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        campaign.deliverables.forEach { deliverable ->
                            DeliverableItem(deliverable)
                        }
                    }
                }
            }

            // Milestones
            item {
                DetailSection(
                    title = "Kilometre Taşları",
                    icon = Icons.Default.Timeline
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        milestones.forEach { milestone ->
                            MilestoneItem(milestone)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CampaignHeaderCard(campaign: CampaignDetail) {
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
                            MaterialTheme.colorScheme.primaryContainer,
                            MaterialTheme.colorScheme.secondaryContainer
                        )
                    )
                )
                .padding(24.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = campaign.title,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = campaign.advertiser,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                        )
                    }
                    AssistChip(
                        onClick = { },
                        label = {
                            Text(
                                text = campaign.status,
                                style = MaterialTheme.typography.labelMedium
                            )
                        },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = Color(0xFF8B5CF6).copy(alpha = 0.2f),
                            labelColor = Color(0xFF8B5CF6)
                        )
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    InfoChip(
                        icon = Icons.Default.AccountBalanceWallet,
                        label = campaign.budget,
                        modifier = Modifier.weight(1f)
                    )
                    InfoChip(
                        icon = Icons.Default.Schedule,
                        label = campaign.deadline,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    campaign.platforms.forEach { platform ->
                        AssistChip(
                            onClick = { },
                            label = { Text(platform, style = MaterialTheme.typography.labelSmall) },
                            colors = AssistChipDefaults.assistChipColors(
                                containerColor = MaterialTheme.colorScheme.surface,
                                labelColor = MaterialTheme.colorScheme.onSurface
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun InfoChip(
    icon: ImageVector,
    label: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Text(
                text = label,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun ProgressSection(progress: Float) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "İlerleme",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${(progress * 100).toInt()}%",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = Color(0xFF8B5CF6),
                trackColor = Color(0xFF8B5CF6).copy(alpha = 0.1f)
            )
        }
    }
}

@Composable
private fun QuickActionsRow(onMessageClick: () -> Unit) {
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
            onClick = { /* Upload content */ },
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(Icons.Default.Upload, contentDescription = null, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("İçerik Yükle")
        }
    }
}

@Composable
private fun DetailSection(
    title: String,
    icon: ImageVector,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 12.dp)
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
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Box(modifier = Modifier.padding(16.dp)) {
                content()
            }
        }
    }
}

@Composable
private fun RequirementItem(requirement: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null,
            tint = Color(0xFF10B981),
            modifier = Modifier.size(20.dp)
        )
        Text(
            text = requirement,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun DeliverableItem(deliverable: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            imageVector = Icons.Default.RadioButtonUnchecked,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(20.dp)
        )
        Text(
            text = deliverable,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun MilestoneItem(milestone: Milestone) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(
                    if (milestone.isCompleted)
                        Color(0xFF10B981).copy(alpha = 0.1f)
                    else
                        MaterialTheme.colorScheme.surfaceVariant
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = if (milestone.isCompleted) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                contentDescription = null,
                tint = if (milestone.isCompleted) Color(0xFF10B981) else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(24.dp)
            )
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = milestone.title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = milestone.date,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = milestone.description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
            )
        }
    }
}
