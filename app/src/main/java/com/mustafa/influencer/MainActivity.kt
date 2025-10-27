package com.mustafa.influencer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.mustafa.influencer.navigation.NavGraph
import com.mustafa.influencer.navigation.Screen
import com.mustafa.influencer.shared.FirebaseManager
import com.mustafa.influencer.ui.theme.InfluencerTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            InfluencerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var isLoading by remember { mutableStateOf(true) }
                    var startDestination by remember { mutableStateOf(Screen.Auth.route) }
                    val scope = rememberCoroutineScope()

                    // Auth state kontrolü
                    LaunchedEffect(Unit) {
                        scope.launch {
                            if (FirebaseManager.isUserLoggedIn()) {
                                val userId = FirebaseManager.getCurrentUserId()
                                if (userId != null) {
                                    val result = FirebaseManager.getUserData(userId)
                                    result.onSuccess { user ->
                                        startDestination = if (user.userType == "influencer") {
                                            Screen.Influencer.route
                                        } else {
                                            Screen.Advertiser.route
                                        }
                                    }.onFailure {
                                        startDestination = Screen.Auth.route
                                    }
                                } else {
                                    startDestination = Screen.Auth.route
                                }
                            } else {
                                startDestination = Screen.Auth.route
                            }
                            isLoading = false
                        }
                    }

                    if (isLoading) {
                        // Loading ekranı
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    } else {
                        val navController = rememberNavController()
                        NavGraph(
                            navController = navController,
                            startDestination = startDestination
                        )
                    }
                }
            }
        }
    }
}