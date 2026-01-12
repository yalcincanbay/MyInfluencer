package com.mustafa.influencer.shared

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mustafa.influencer.data.User
import kotlinx.coroutines.tasks.await

object FirebaseManager {

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    private const val USERS = "users"

    fun isUserLoggedIn(): Boolean = auth.currentUser != null
    fun getCurrentUserId(): String? = auth.currentUser?.uid
    fun signOut() = auth.signOut()

    suspend fun signUpWithEmail(
        email: String,
        password: String,
        userType: String
    ): Result<String> {
        return try {
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            val userId = authResult.user?.uid ?: error("User ID alınamadı")

            // Yeni kullanıcı dokümanı - profileCompleted false başlasın
            val userMap = hashMapOf(
                "email" to email,
                "userType" to userType,
                "profileCompleted" to false,
                "platforms" to emptyList<String>(),
                "platformLinks" to emptyMap<String, String>(),
                "categories" to emptyList<String>(),
                "bio" to "",
                "companyName" to ""
            )

            firestore.collection(USERS).document(userId).set(userMap).await()
            Result.success(userId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun signInWithEmail(email: String, password: String): Result<String> {
        return try {
            val authResult = auth.signInWithEmailAndPassword(email, password).await()
            val userId = authResult.user?.uid ?: error("User ID alınamadı")

            // Eğer users dokümanı yoksa (nadiren olur) oluştur
            ensureUserDocumentExists(userId, email)

            Result.success(userId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private suspend fun ensureUserDocumentExists(userId: String, email: String) {
        val doc = firestore.collection(USERS).document(userId).get().await()
        if (!doc.exists()) {
            val fallback = hashMapOf(
                "email" to email,
                "userType" to "influencer", // default; istersen "unknown" yap
                "profileCompleted" to false
            )
            firestore.collection(USERS).document(userId).set(fallback).await()
        }
    }

    suspend fun getUserData(userId: String): Result<User> {
        return try {
            val document = firestore.collection(USERS).document(userId).get().await()
            if (!document.exists()) error("Kullanıcı bulunamadı (users/$userId)")

            val user = User(
                email = document.getString("email") ?: "",
                userType = document.getString("userType") ?: "",
                profileCompleted = document.getBoolean("profileCompleted") ?: false,
                platforms = document.get("platforms") as? List<String> ?: emptyList(),
                platformLinks = document.get("platformLinks") as? Map<String, String> ?: emptyMap(),
                categories = document.get("categories") as? List<String> ?: emptyList(),
                bio = document.getString("bio") ?: "",
                companyName = document.getString("companyName") ?: ""
            )

            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun saveInfluencerProfile(
        platforms: List<String>,
        platformLinks: Map<String, String>,
        categories: List<String>,
        bio: String
    ): Result<Unit> {
        return try {
            val userId = getCurrentUserId() ?: error("Kullanıcı ID'si bulunamadı")

            val updateMap = hashMapOf<String, Any>(
                "platforms" to platforms,
                "platformLinks" to platformLinks,
                "categories" to categories,
                "bio" to bio,
                "profileCompleted" to true
            )

            firestore.collection(USERS).document(userId).update(updateMap).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun saveAdvertiserProfile(
        companyName: String,
        platforms: List<String>,
        categories: List<String>
    ): Result<Unit> {
        return try {
            val userId = getCurrentUserId() ?: error("Kullanıcı ID'si bulunamadı")

            val updateMap = hashMapOf<String, Any>(
                "companyName" to companyName,
                "platforms" to platforms,
                "categories" to categories,
                "profileCompleted" to true
            )

            firestore.collection(USERS).document(userId).update(updateMap).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
