package com.mustafa.influencer.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.mustafa.influencer.advertiser.AdvertiserScreen
import com.mustafa.influencer.influencer.InfluencerScreen

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
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
                }
            )
        }

        composable(Screen.Advertiser.route) {
            AdvertiserScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}
