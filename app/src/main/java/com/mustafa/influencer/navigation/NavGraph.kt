package com.mustafa.influencer.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.mustafa.influencer.advertiser.AdvertiserProfileSetupScreen
import com.mustafa.influencer.advertiser.AdvertiserScreen
import com.mustafa.influencer.advertiser.CreateCampaignScreen
import com.mustafa.influencer.advertiser.InfluencerSearchScreen
import com.mustafa.influencer.advertiser.ReportsScreen
import com.mustafa.influencer.auth.AuthScreen
import com.mustafa.influencer.influencer.InfluencerScreen
import com.mustafa.influencer.influencer.ProfileSetupScreen

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
                    // Kullanıcı başarıyla giriş yaptı
                    // Her iki tip için de ProfileSetup'a git
                    val destination = if (userType == "influencer") {
                        Screen.ProfileSetup.route
                    } else {
                        Screen.AdvertiserProfileSetup.route
                    }

                    // Auth ekranını stack'ten kaldır ve ilgili ekrana git
                    navController.navigate(destination) {
                        popUpTo(Screen.Auth.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.ProfileSetup.route) {
            ProfileSetupScreen(
                onSetupComplete = {
                    // Profil kurulumu tamamlandı, Influencer ekranına git
                    navController.navigate(Screen.Influencer.route) {
                        popUpTo(Screen.ProfileSetup.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.AdvertiserProfileSetup.route) {
            AdvertiserProfileSetupScreen(
                onSetupComplete = {
                    // Profil kurulumu tamamlandı, Advertiser ekranına git
                    navController.navigate(Screen.Advertiser.route) {
                        popUpTo(Screen.AdvertiserProfileSetup.route) { inclusive = true }
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
                },
                onInfluencerSearchClick = {
                    navController.navigate(Screen.InfluencerSearch.route)
                },
                onCreateCampaignClick = {
                    navController.navigate(Screen.CreateCampaign.route)
                },
                onReportsClick = {
                    navController.navigate(Screen.Reports.route)
                }
            )
        }

        composable(Screen.InfluencerSearch.route) {
            InfluencerSearchScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.CreateCampaign.route) {
            CreateCampaignScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.Reports.route) {
            ReportsScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}
