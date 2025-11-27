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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

// Mock Data Model
data class Message(
    val advertiserName: String,
    val lastMessage: String,
    val timestamp: String,
    val unreadCount: Int = 0,
    val isOnline: Boolean = false,
    val avatarColor: Color
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessagesScreen(
    onBackClick: () -> Unit,
    onMessageClick: (String) -> Unit = {}
) {
    var searchQuery by remember { mutableStateOf("") }

    // Mock Messages
    val allMessages = listOf(
        Message(
            advertiserName = "TechVision A.Ş.",
            lastMessage = "Merhaba, kampanya detayları için görüşelim mi?",
            timestamp = "5 dk önce",
            unreadCount = 2,
            isOnline = true,
            avatarColor = Color(0xFF6366F1)
        ),
        Message(
            advertiserName = "BeautyPro",
            lastMessage = "İçerik taslağını inceledim, harika görünüyor!",
            timestamp = "1 saat önce",
            unreadCount = 0,
            isOnline = true,
            avatarColor = Color(0xFFEC4899)
        ),
        Message(
            advertiserName = "GameZone",
            lastMessage = "Canlı yayın için saat kaçta müsaitsiniz?",
            timestamp = "2 saat önce",
            unreadCount = 1,
            isOnline = false,
            avatarColor = Color(0xFF8B5CF6)
        ),
        Message(
            advertiserName = "FitLife",
            lastMessage = "30 günlük challenge için ürünlerimizi gönderdik",
            timestamp = "Dün",
            unreadCount = 0,
            isOnline = false,
            avatarColor = Color(0xFF10B981)
        ),
        Message(
            advertiserName = "StyleHub",
            lastMessage = "Yeni koleksiyon çekimi pazartesi günü olacak",
            timestamp = "2 gün önce",
            unreadCount = 0,
            isOnline = false,
            avatarColor = Color(0xFFF59E0B)
        ),
        Message(
            advertiserName = "FoodieSpot",
            lastMessage = "Teşekkürler! İşbirliğinden çok memnun kaldık",
            timestamp = "1 hafta önce",
            unreadCount = 0,
            isOnline = false,
            avatarColor = Color(0xFFEF4444)
        )
    )

    val filteredMessages = allMessages.filter {
        searchQuery.isEmpty() || it.advertiserName.contains(searchQuery, ignoreCase = true) ||
                it.lastMessage.contains(searchQuery, ignoreCase = true)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mesajlar") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Geri")
                    }
                },
                actions = {
                    IconButton(onClick = { /* Filter messages */ }) {
                        Icon(Icons.Default.FilterList, contentDescription = "Filtrele")
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
            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                placeholder = { Text("Mesajlarda ara...") },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = null)
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(Icons.Default.Clear, contentDescription = "Temizle")
                        }
                    }
                },
                shape = RoundedCornerShape(16.dp),
                singleLine = true
            )

            // Messages List
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                if (filteredMessages.isEmpty()) {
                    item {
                        EmptyMessagesState()
                    }
                } else {
                    items(filteredMessages) { message ->
                        MessageCard(
                            message = message,
                            onClick = { onMessageClick(message.advertiserName) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MessageCard(
    message: Message,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 6.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (message.unreadCount > 0) {
                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
            } else {
                MaterialTheme.colorScheme.surface
            }
        ),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar
            Box {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(message.avatarColor),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Business,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }
                // Online Indicator
                if (message.isOnline) {
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF10B981))
                            .align(Alignment.BottomEnd)
                    )
                }
            }

            // Message Content
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = message.advertiserName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = if (message.unreadCount > 0) FontWeight.Bold else FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = message.timestamp,
                        style = MaterialTheme.typography.labelSmall,
                        color = if (message.unreadCount > 0) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        }
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = message.lastMessage,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )

                    if (message.unreadCount > 0) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Badge(
                            containerColor = MaterialTheme.colorScheme.primary
                        ) {
                            Text(
                                text = message.unreadCount.toString(),
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptyMessagesState() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 40.dp, vertical = 60.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.Message,
            contentDescription = null,
            modifier = Modifier.size(80.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Henüz mesaj yok",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Advertiser'larla görüşmeye başladığınızda mesajlarınız burada görünecek",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}
