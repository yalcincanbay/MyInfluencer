package com.mustafa.influencer.domain.model

data class Campaign(
    val id: String = "",
    val advertiserId: String = "",
    val advertiserName: String = "",
    val title: String = "",
    val description: String = "",
    val platform: String = "",
    val category: String = "",
    val budget: Long = 0L,
    val deadlineText: String = "",
    val status: String = "active", // active | draft | closed
    val createdAt: Long = 0L
)
