package com.mustafa.influencer.navigation

sealed class Screen(val route: String) {
    object Auth : Screen("auth")
    object ProfileSetup : Screen("profile_setup")
    object AdvertiserProfileSetup : Screen("advertiser_profile_setup")
    object Home : Screen("home")
    object Influencer : Screen("influencer")
    object Advertiser : Screen("advertiser")
}
