package com.mustafa.influencer.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.mustafa.influencer.advertiser.AdvertiserScreen
import com.mustafa.influencer.auth.AuthScreen
import com.mustafa.influencer.influencer.InfluencerScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String = Screen.Auth.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Auth.route) {
            AuthScreen(
                onAuthSuccess = { userType ->
                    // Kullanıcı başarıyla giriş yaptı, userType'a göre yönlendir
                    val destination = if (userType == "influencer") {
                        Screen.Influencer.route
                    } else {
                        Screen.Advertiser.route
                    }

                    // Auth ekranını stack'ten kaldır ve ilgili ekrana git
                    navController.navigate(destination) {
                        popUpTo(Screen.Auth.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Home.route) {
            HomeScreen(
                onInfluencerClick = {
                    navController.navigate(Screen.Influencer.route)
                },
                onAdvertiserClick = {
                    navController.navigate(Screen.Advertiser.route)
                }
            )
        }

        composable(Screen.Influencer.route) {
            InfluencerScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onLogout = {
                    com.mustafa.influencer.shared.FirebaseManager.signOut()
                    navController.navigate(Screen.Auth.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Advertiser.route) {
            AdvertiserScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onLogout = {
                    com.mustafa.influencer.shared.FirebaseManager.signOut()
                    navController.navigate(Screen.Auth.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    }
}
