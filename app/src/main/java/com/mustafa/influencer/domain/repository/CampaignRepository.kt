package com.mustafa.influencer.domain.repository

import com.mustafa.influencer.domain.model.Campaign

interface CampaignRepository {
    suspend fun listActiveCampaigns(): List<Campaign>
    suspend fun getCampaign(id: String): Campaign
    suspend fun createCampaign(
        title: String,
        description: String,
        platform: String,
        category: String,
        budget: Long,
        deadlineText: String,
        status: String
    ): String
}
