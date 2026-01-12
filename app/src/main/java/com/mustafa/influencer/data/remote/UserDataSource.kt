package com.mustafa.influencer.data.remote

import com.google.firebase.firestore.FirebaseFirestore
import com.mustafa.influencer.data.User
import com.mustafa.influencer.domain.model.UserType
import com.mustafa.influencer.shared.bool
import com.mustafa.influencer.shared.string
import com.mustafa.influencer.shared.stringList
import com.mustafa.influencer.shared.stringMap
import kotlinx.coroutines.tasks.await

class UserDataSource(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    private val users = firestore.collection("users")

    suspend fun createMinimalUser(userId: String, email: String, userType: UserType) {
        val data = mapOf(
            "email" to email,
            "userType" to userType.name,
            "profileCompleted" to false
        )
        users.document(userId).set(data).await()
    }

    suspend fun getUser(userId: String): User {
        val doc = users.document(userId).get().await()
        if (!doc.exists()) error("Kullanıcı bulunamadı")

        return User(
            email = doc.string("email"),
            userType = doc.string("userType"),
            profileCompleted = doc.bool("profileCompleted"),
            platforms = doc.stringList("platforms"),
            platformLinks = doc.stringMap("platformLinks"),
            categories = doc.stringList("categories"),
            bio = doc.string("bio"),
            companyName = doc.string("companyName")
        )
    }

    suspend fun updateInfluencerProfile(
        userId: String,
        platforms: List<String>,
        platformLinks: Map<String, String>,
        categories: List<String>,
        bio: String
    ) {
        val profileData = mapOf(
            "platforms" to platforms,
            "platformLinks" to platformLinks,
            "categories" to categories,
            "bio" to bio,
            "profileCompleted" to true
        )
        users.document(userId).update(profileData).await()
    }

    suspend fun updateAdvertiserProfile(
        userId: String,
        companyName: String,
        platforms: List<String>,
        categories: List<String>
    ) {
        val profileData = mapOf(
            "companyName" to companyName,
            "platforms" to platforms,
            "categories" to categories,
            "profileCompleted" to true
        )
        users.document(userId).update(profileData).await()
    }
}
