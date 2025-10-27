package com.mustafa.influencer.navigation

sealed class Screen(val route: String) {
    object Auth : Screen("auth")
    object Home : Screen("home")
    object Influencer : Screen("influencer")
    object Advertiser : Screen("advertiser")
}
