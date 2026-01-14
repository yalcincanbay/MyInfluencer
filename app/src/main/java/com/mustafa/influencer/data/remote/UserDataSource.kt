package com.mustafa.influencer.data.remote

import com.google.firebase.firestore.FirebaseFirestore
import com.mustafa.influencer.data.User
import com.mustafa.influencer.domain.model.UserType
import kotlinx.coroutines.tasks.await

class UserDataSource {
    private val db = FirebaseFirestore.getInstance()
    private val usersCollection = db.collection("users")

    suspend fun createMinimalUser(uid: String, email: String, userType: UserType) {
        // Yeni User modeline uygun obje oluşturuyoruz
        val user = User(
            id = uid,
            email = email,
            userType = userType.name.lowercase(), // Enum'ı string'e çeviriyoruz
            profileCompleted = false,
            createdAt = System.currentTimeMillis(),
            name = "", // Başlangıçta boş
            followerCount = "",
            priceRange = ""
        )
        usersCollection.document(uid).set(user).await()
    }

    suspend fun getUser(userId: String): User {
        val snapshot = usersCollection.document(userId).get().await()
        // Firestore verisini User sınıfına dönüştür
        val user = snapshot.toObject(User::class.java) ?: error("Kullanıcı bulunamadı")

        // Eğer ID boş gelirse (eski kayıtlarda olabilir), doküman ID'sini ata
        return if (user.id.isEmpty()) user.copy(id = snapshot.id) else user
    }

    suspend fun updateInfluencerProfile(
        userId: String,
        platforms: List<String>,
        platformLinks: Map<String, String>,
        categories: List<String>,
        bio: String
    ) {
        val updates = mapOf(
            "platforms" to platforms,
            "platformLinks" to platformLinks,
            "categories" to categories,
            "bio" to bio,
            "profileCompleted" to true
        )
        usersCollection.document(userId).update(updates).await()
    }

    suspend fun updateAdvertiserProfile(
        userId: String,
        companyName: String,
        platforms: List<String>,
        categories: List<String>
    ) {
        val updates = mapOf(
            "companyName" to companyName,
            "platforms" to platforms,
            "categories" to categories,
            "profileCompleted" to true
        )
        usersCollection.document(userId).update(updates).await()
    }

    // Arama ekranı için tüm influencerları getirir
    suspend fun getAllInfluencers(): List<User> {
        return try {
            val snapshot = usersCollection
                .whereEqualTo("userType", "influencer")
                .get()
                .await()

            snapshot.toObjects(User::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}