package com.mustafa.influencer.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.mustafa.influencer.shared.FirebaseManager

@Composable
fun LoadingRouterScreen(
    onResolved: (String) -> Unit,
    onFail: () -> Unit
) {
    LaunchedEffect(Unit) {

        if (!FirebaseManager.isUserLoggedIn()) {
            onFail()
            return@LaunchedEffect
        }

        val uid = FirebaseManager.getCurrentUserId()
        if (uid.isNullOrBlank()) {
            onFail()
            return@LaunchedEffect
        }

        val result = FirebaseManager.getUserData(uid)

        result
            .onSuccess { user ->
                val destination = when (user.userType.lowercase()) {
                    "influencer" -> {
                        if (user.profileCompleted) Screen.Influencer.route
                        else Screen.ProfileSetup.route
                    }
                    "advertiser" -> {
                        if (user.profileCompleted) Screen.Advertiser.route
                        else Screen.AdvertiserProfileSetup.route
                    }
                    else -> Screen.Auth.route
                }

                onResolved(destination)
            }
            .onFailure {
                onFail()
            }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}
