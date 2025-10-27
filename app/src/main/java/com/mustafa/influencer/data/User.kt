package com.mustafa.influencer.data

data class User(
    val email: String = "",
    val userType: String = "", // "influencer" veya "advertiser"
    val profileCompleted: Boolean = false,

    // Influencer-specific fields
    val platforms: List<String> = emptyList(), // ["youtube", "tiktok", "instagram"]
    val platformLinks: Map<String, String> = emptyMap(), // {"youtube": "link", "tiktok": "link"}
    val categories: List<String> = emptyList(), // ["spor", "makyaj", "elektronik"]
    val bio: String = "", // Profil açıklaması

    // Advertiser-specific fields
    val companyName: String = "" // Şirket ismi
)
