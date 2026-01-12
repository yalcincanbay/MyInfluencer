package com.mustafa.influencer.data.remote

import com.google.firebase.firestore.FirebaseFirestore
import com.mustafa.influencer.domain.model.Campaign
import com.mustafa.influencer.shared.long
import com.mustafa.influencer.shared.string
import kotlinx.coroutines.tasks.await

class CampaignDataSource(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    private val campaigns = firestore.collection("campaigns")

    suspend fun createCampaign(
        campaign: Map<String, Any>
    ): String {
        val ref = campaigns.add(campaign).await()
        return ref.id
    }

    suspend fun getCampaign(campaignId: String): Campaign {
        val doc = campaigns.document(campaignId).get().await()
        if (!doc.exists()) error("Kampanya bulunamadı")

        return Campaign(
            id = doc.id,
            advertiserId = doc.string("advertiserId"),
            advertiserName = doc.string("advertiserName"),
            title = doc.string("title"),
            description = doc.string("description"),
            platform = doc.string("platform"),
            category = doc.string("category"),
            budget = doc.long("budget"),
            deadlineText = doc.string("deadlineText"),
            status = doc.string("status").ifBlank { "active" },
            createdAt = doc.long("createdAt")
        )
    }

    suspend fun listActiveCampaigns(): List<Campaign> {
        val snap = campaigns
            .whereEqualTo("status", "active")
            .orderBy("createdAt", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .get()
            .await()

        return snap.documents.map { doc ->
            Campaign(
                id = doc.id,
                advertiserId = doc.string("advertiserId"),
                advertiserName = doc.string("advertiserName"),
                title = doc.string("title"),
                description = doc.string("description"),
                platform = doc.string("platform"),
                category = doc.string("category"),
                budget = doc.long("budget"),
                deadlineText = doc.string("deadlineText"),
                status = doc.string("status").ifBlank { "active" },
                createdAt = doc.long("createdAt")
            )
        }
    }
}
