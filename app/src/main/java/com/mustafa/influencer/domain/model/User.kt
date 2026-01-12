package com.mustafa.influencer.domain.model

data class User(
    val email: String = "",
    val userType: String = "",
    val profileCompleted: Boolean = false,
    val platforms: List<String> = emptyList(),
    val platformLinks: Map<String, String> = emptyMap(),
    val categories: List<String> = emptyList(),
    val bio: String = "",
    val companyName: String = ""
)
