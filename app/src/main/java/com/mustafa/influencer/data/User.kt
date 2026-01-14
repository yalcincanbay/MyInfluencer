package com.mustafa.influencer.data

data class User(
    // --- Temel Alanlar ---
    val id: String = "",            // Yeni eklendi (Gerekli)
    val email: String = "",
    val userType: String = "",      // "influencer" veya "advertiser"
    val profileCompleted: Boolean = false,
    val name: String = "",          // Yeni eklendi (Arama ekranı için gerekli)
    val createdAt: Long = 0L,       // Yeni eklendi (Sıralama için gerekli)

    // --- Influencer Özel Alanları ---
    val platforms: List<String> = emptyList(),
    val platformLinks: Map<String, String> = emptyMap(),
    val categories: List<String> = emptyList(),
    val bio: String = "",

    // Arama ekranında filtreleme yapmak için bu alanlar gerekli:
    val followerCount: String = "", // Örn: "125K"
    val priceRange: String = "",    // Örn: "₺5.000 - ₺10.000"

    // --- Advertiser Özel Alanları ---
    val companyName: String = ""
)