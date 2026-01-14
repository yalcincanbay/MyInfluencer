package com.mustafa.influencer.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.mustafa.influencer.data.remote.CampaignDataSource
import com.mustafa.influencer.domain.model.Campaign
import com.mustafa.influencer.domain.repository.CampaignRepository
import com.mustafa.influencer.shared.FirebaseManager
import kotlinx.coroutines.tasks.await

class CampaignRepositoryImpl(
    private val ds: CampaignDataSource = CampaignDataSource()
) : CampaignRepository {

    override suspend fun listActiveCampaigns(): List<Campaign> = ds.listActiveCampaigns()

    override suspend fun getCampaign(id: String): Campaign = ds.getCampaign(id)

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

        // --- DÜZELTME BAŞLANGICI ---
        // Kampanyayı kaydetmeden önce, kullanıcının profilinden "companyName" bilgisini çekiyoruz.
        var companyName = "Gizli Marka"
        try {
            val db = FirebaseFirestore.getInstance()
            val userDoc = db.collection("users").document(uid).get().await()
            // Eğer companyName boşsa veya null ise varsayılan bir isim kullanma, direkt boş gelsin UI halleder
            companyName = userDoc.getString("companyName") ?: ""

            // Eğer şirket adı profilde de boşsa, kullanıcının "name" alanını deneyelim
            if (companyName.isBlank()) {
                companyName = userDoc.getString("name") ?: "İsimsiz Marka"
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        // --- DÜZELTME BİTİŞİ ---

        val data = mapOf(
            "advertiserId" to uid,
            "advertiserName" to companyName, // Artık gerçek şirket adını kaydediyoruz
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