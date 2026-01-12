package com.mustafa.influencer.navigation

sealed class Screen(val route: String) {
    object Auth : Screen("auth")
    object LoadingRouter : Screen("loading_router")

    object ProfileSetup : Screen("profile_setup")
    object AdvertiserProfileSetup : Screen("advertiser_profile_setup")

    object Home : Screen("home")
    object Influencer : Screen("influencer")
    object Advertiser : Screen("advertiser")

    object InfluencerSearch : Screen("influencer_search")
    object CreateCampaign : Screen("create_campaign")
    object Reports : Screen("reports")

    object CampaignSearch : Screen("campaign_search")
    object Messages : Screen("messages")
    object Statistics : Screen("statistics")

    object CampaignDetail : Screen("campaign_detail/{campaignId}") {
        fun createRoute(campaignId: String) = "campaign_detail/$campaignId"
    }

    object AdvertiserDetail : Screen("advertiser_detail/{advertiserId}") {
        fun createRoute(advertiserId: String) = "advertiser_detail/$advertiserId"
    }
}
