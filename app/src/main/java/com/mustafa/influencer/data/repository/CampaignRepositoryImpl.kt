package com.mustafa.influencer.data.repository

import com.mustafa.influencer.data.remote.CampaignDataSource
import com.mustafa.influencer.domain.model.Campaign
import com.mustafa.influencer.domain.repository.CampaignRepository
import com.mustafa.influencer.shared.FirebaseManager

class CampaignRepositoryImpl(
    private val ds: CampaignDataSource = CampaignDataSource()
) : CampaignRepository {

    override suspend fun listActiveCampaigns(): List<Campaign> = ds.listActiveCampaigns()

    override suspend fun getCampaign(id: String): Campaign = ds.getCampaign(id)

    // --- YENİ EKLENEN ---
    override suspend fun getCampaignsByAdvertiser(advertiserId: String): List<Campaign> {
        return ds.getCampaignsByAdvertiser(advertiserId)
    }

    override suspend fun createCampaign(
        title: String,
        description: String,
        platform: String,
        category: String,
        budget: Long,
        deadlineText: String,
        status: String
    ): String {
        val uid = FirebaseManager.getCurrentUserId() ?: error("Oturum yok")

        val data = mapOf(
            "advertiserId" to uid,
            "advertiserName" to "",
            "title" to title.trim(),
            "description" to description.trim(),
            "platform" to platform,
            "category" to category,
            "budget" to budget,
            "deadlineText" to deadlineText.trim(),
            "status" to status,
            "createdAt" to System.currentTimeMillis()
        )

        return ds.createCampaign(data)
    }
}