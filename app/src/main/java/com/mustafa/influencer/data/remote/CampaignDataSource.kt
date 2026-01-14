package com.mustafa.influencer.data.remote

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.mustafa.influencer.domain.model.Campaign
import com.mustafa.influencer.domain.model.Milestone
import com.mustafa.influencer.shared.long
import com.mustafa.influencer.shared.string
import com.mustafa.influencer.shared.stringList
import kotlinx.coroutines.tasks.await

class CampaignDataSource(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    // BURAYA DİKKAT: Koleksiyon adı 'campaigns'. Firestore'da da birebir aynı olmalı.
    private val campaigns = firestore.collection("campaigns")

    suspend fun createCampaign(campaign: Map<String, Any>): String {
        val ref = campaigns.add(campaign).await()
        return ref.id
    }

    suspend fun getCampaign(campaignId: String): Campaign {
        val doc = campaigns.document(campaignId).get().await()
        if (!doc.exists()) error("Kampanya bulunamadı")

        // Firestore'dan gelen platform verisi tekil string ise listeye çevir, liste ise direkt al
        val platformStr = doc.string("platform")
        val platformsList = if(platformStr.isNotEmpty()) listOf(platformStr) else doc.stringList("platforms")

        return Campaign(
            id = doc.id,
            advertiserId = doc.string("advertiserId"),
            advertiserName = doc.string("advertiserName"),
            title = doc.string("title"),
            description = doc.string("description"),
            platform = platformStr,
            platforms = platformsList,
            category = doc.string("category"),
            budget = doc.long("budget"),
            deadlineText = doc.string("deadlineText"),
            status = doc.string("status").ifBlank { "active" },
            createdAt = doc.long("createdAt"),

            // --- Geri kalanlar şu an DB'de olmadığı için Mock Data ile dolduruyoruz ---
            progress = 0.3f, // Varsayılan %30
            requirements = listOf("Min 10K Takipçi", "Düzenli içerik üretimi", "Yüksek etkileşim oranı"),
            deliverables = listOf("1x Reels Video", "3x Story Paylaşımı"),
            milestones = listOf(
                Milestone("Anlaşma", "Bugün", true, "Kampanya kabul edildi"),
                Milestone("İçerik Üretimi", "3 Gün Sonra", false, "Video çekimi ve kurgu"),
                Milestone("Yayın", "1 Hafta Sonra", false, "İçeriğin paylaşılması")
            )
        )
    }

    suspend fun listActiveCampaigns(): List<Campaign> {
        return try {
            Log.d("FirebaseDebug", "Veri çekme işlemi başladı...")

            val snap = campaigns
                // Geçici olarak filtreleri kapalı tutalım, önce bağlantıyı test edelim
                // .whereEqualTo("status", "active")
                // .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
                .await()

            Log.d("FirebaseDebug", "Firestore yanıt verdi. Doküman sayısı: ${snap.size()}")

            if (snap.isEmpty) {
                Log.w("FirebaseDebug", "HATA: Koleksiyon bulundu ama içi boş veya filtreye uyan yok.")
            }

            val list = snap.documents.map { doc ->
                // Her bir dokümanı loglayalım
                Log.d("FirebaseDebug", "Okunan Doküman ID: ${doc.id} - Status: ${doc.get("status")}")

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
            Log.d("FirebaseDebug", "Dönüştürme tamamlandı. Liste boyutu: ${list.size}")
            list

        } catch (e: Exception) {
            // İŞTE HATAYI BURADA GÖRECEĞİZ
            Log.e("FirebaseDebug", "CRITICAL ERROR: Veri çekilemedi!", e)
            emptyList()
        }
    }
}