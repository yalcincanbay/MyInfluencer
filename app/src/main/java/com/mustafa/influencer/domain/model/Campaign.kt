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
    val createdAt: Long = 0L,
// UI için yeni eklenen alanlar (Firebase'de yoksa varsayılan değerler çalışır)
    val progress: Float = 0.0f,
    val requirements: List<String> = emptyList(),
    val deliverables: List<String> = emptyList(),
    val milestones: List<Milestone> = emptyList(),
    val platforms: List<String> = emptyList()
)

data class Milestone(
    val title: String = "",
    val date: String = "",
    val isCompleted: Boolean = false,
    val description: String = ""
)
