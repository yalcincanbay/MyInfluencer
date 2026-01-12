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
import com.mustafa.influencer.influencer.*
import com.mustafa.influencer.shared.FirebaseManager

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String = Screen.Auth.route
) {
    NavHost(navController = navController, startDestination = startDestination) {

        composable(Screen.Auth.route) {
            AuthScreen(
                onAuthSuccess = {
                    val uid = FirebaseManager.getCurrentUserId()
                    if (uid == null) {
                        navController.navigate(Screen.Auth.route) {
                            popUpTo(0) { inclusive = true }
                        }
                        return@AuthScreen
                    }

                    // Firestore'dan userType + profileCompleted okuyup route seç
                    navController.navigate(Screen.LoadingRouter.route) {
                        popUpTo(Screen.Auth.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.LoadingRouter.route) {
            LoadingRouterScreen(
                onResolved = { destination ->
                    navController.navigate(destination) {
                        popUpTo(Screen.LoadingRouter.route) { inclusive = true }
                    }
                },
                onFail = {
                    navController.navigate(Screen.Auth.route) {
                        popUpTo(Screen.LoadingRouter.route) { inclusive = true }
                    }
                }
            )
        }


        composable(Screen.ProfileSetup.route) {
            ProfileSetupScreen(
                onSetupComplete = {
                    navController.navigate(Screen.Influencer.route) {
                        popUpTo(Screen.ProfileSetup.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.AdvertiserProfileSetup.route) {
            AdvertiserProfileSetupScreen(
                onSetupComplete = {
                    navController.navigate(Screen.Advertiser.route) {
                        popUpTo(Screen.AdvertiserProfileSetup.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Influencer.route) {
            InfluencerScreen(
                onBackClick = { navController.popBackStack() },
                onLogout = {
                    FirebaseManager.signOut()
                    navController.navigate(Screen.Auth.route) { popUpTo(0) { inclusive = true } }
                },
                onCampaignSearchClick = { navController.navigate(Screen.CampaignSearch.route) },
                onMessagesClick = { navController.navigate(Screen.Messages.route) },
                onStatisticsClick = { navController.navigate(Screen.Statistics.route) },
                onCampaignClick = { campaignId -> navController.navigate(Screen.CampaignDetail.createRoute(campaignId)) },
                onAdvertiserClick = { advertiserId -> navController.navigate(Screen.AdvertiserDetail.createRoute(advertiserId)) }
            )
        }

        composable(Screen.Advertiser.route) {
            AdvertiserScreen(
                onBackClick = { navController.popBackStack() },
                onLogout = {
                    FirebaseManager.signOut()
                    navController.navigate(Screen.Auth.route) { popUpTo(0) { inclusive = true } }
                },
                onInfluencerSearchClick = { navController.navigate(Screen.InfluencerSearch.route) },
                onCreateCampaignClick = { navController.navigate(Screen.CreateCampaign.route) },
                onReportsClick = { navController.navigate(Screen.Reports.route) }
            )
        }

        composable(Screen.InfluencerSearch.route) { InfluencerSearchScreen(onBackClick = { navController.popBackStack() }) }
        composable(Screen.CreateCampaign.route) { CreateCampaignScreen(onBackClick = { navController.popBackStack() }) }
        composable(Screen.Reports.route) { ReportsScreen(onBackClick = { navController.popBackStack() }) }

        composable(Screen.CampaignSearch.route) {
            CampaignSearchScreen(
                onBackClick = { navController.popBackStack() },
                onCampaignClick = { campaignId -> navController.navigate(Screen.CampaignDetail.createRoute(campaignId)) }
            )
        }

        composable(Screen.Messages.route) { MessagesScreen(onBackClick = { navController.popBackStack() }) }
        composable(Screen.Statistics.route) { StatisticsScreen(onBackClick = { navController.popBackStack() }) }

        composable(Screen.CampaignDetail.route) { backStackEntry ->
            val campaignId = backStackEntry.arguments?.getString("campaignId") ?: ""
            CampaignDetailScreen(
                campaignId = campaignId,
                onBackClick = { navController.popBackStack() },
                onMessageClick = { navController.navigate(Screen.Messages.route) }
            )
        }

        composable(Screen.AdvertiserDetail.route) { backStackEntry ->
            val advertiserId = backStackEntry.arguments?.getString("advertiserId") ?: ""
            AdvertiserDetailScreen(
                advertiserId = advertiserId,
                onBackClick = { navController.popBackStack() },
                onMessageClick = { navController.navigate(Screen.Messages.route) },
                onCampaignClick = { campaignId -> navController.navigate(Screen.CampaignDetail.createRoute(campaignId)) }
            )
        }
    }
}
