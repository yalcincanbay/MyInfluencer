package com.mustafa.influencer.influencer

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InfluencerScreen(
    onBackClick: () -> Unit,
    onLogout: () -> Unit = {},
    onCampaignSearchClick: () -> Unit = {},
    onMessagesClick: () -> Unit = {},
    onStatisticsClick: () -> Unit = {},
    onCampaignClick: (String) -> Unit = {},
    onAdvertiserClick: (String) -> Unit = {}
) {
    var selectedTab by remember { mutableStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = when (selectedTab) {
                            0 -> "Ana Sayfa"
                            1 -> "Profil"
                            else -> "Influencer"
                        }
                    )
                }
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Home, contentDescription = "Ana Sayfa") },
                    label = { Text("Ana Sayfa") },
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Person, contentDescription = "Profil") },
                    label = { Text("Profil") },
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 }
                )
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when (selectedTab) {
                0 -> InfluencerHomeScreen(
                    onCampaignSearchClick = onCampaignSearchClick,
                    onMessagesClick = onMessagesClick,
                    onStatisticsClick = onStatisticsClick,
                    onCampaignClick = onCampaignClick,
                    onAdvertiserClick = onAdvertiserClick
                )
                1 -> InfluencerProfileScreen(onLogout = onLogout)
            }
        }
    }
}
