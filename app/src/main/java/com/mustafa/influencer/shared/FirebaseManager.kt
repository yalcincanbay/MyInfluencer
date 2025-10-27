package com.mustafa.influencer.shared

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mustafa.influencer.data.User
import kotlinx.coroutines.tasks.await

object FirebaseManager {

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    // Kullanıcı girişli mi kontrol et
    fun isUserLoggedIn(): Boolean {
        return auth.currentUser != null
    }

    // Mevcut kullanıcının ID'sini al
    fun getCurrentUserId(): String? {
        return auth.currentUser?.uid
    }

    // E-posta ile kayıt
    suspend fun signUpWithEmail(
        email: String,
        password: String,
        userType: String
    ): Result<String> {
        return try {
            // Firebase Authentication ile kullanıcı oluştur
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            val userId = authResult.user?.uid ?: throw Exception("User ID alınamadı")

            // Firestore'a kullanıcı verilerini kaydet (minimal veri)
            val user = hashMapOf(
                "email" to email,
                "userType" to userType
            )

            firestore.collection("users")
                .document(userId)
                .set(user)
                .await()

            Result.success(userId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // E-posta ile giriş
    suspend fun signInWithEmail(email: String, password: String): Result<String> {
        return try {
            val authResult = auth.signInWithEmailAndPassword(email, password).await()
            val userId = authResult.user?.uid ?: throw Exception("User ID alınamadı")
            Result.success(userId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Kullanıcı verilerini getir
    suspend fun getUserData(userId: String): Result<User> {
        return try {
            val document = firestore.collection("users")
                .document(userId)
                .get()
                .await()

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

    // Influencer profil bilgilerini kaydet/güncelle
    suspend fun saveInfluencerProfile(
        platforms: List<String>,
        platformLinks: Map<String, String>,
        categories: List<String>,
        bio: String
    ): Result<Unit> {
        return try {
            val userId = getCurrentUserId() ?: throw Exception("Kullanıcı ID'si bulunamadı")

            val profileData = hashMapOf(
                "platforms" to platforms,
                "platformLinks" to platformLinks,
                "categories" to categories,
                "bio" to bio,
                "profileCompleted" to true
            )

            firestore.collection("users")
                .document(userId)
                .update(profileData as Map<String, Any>)
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Advertiser profil bilgilerini kaydet/güncelle
    suspend fun saveAdvertiserProfile(
        companyName: String,
        platforms: List<String>,
        categories: List<String>
    ): Result<Unit> {
        return try {
            val userId = getCurrentUserId() ?: throw Exception("Kullanıcı ID'si bulunamadı")

            val profileData = hashMapOf(
                "companyName" to companyName,
                "platforms" to platforms,
                "categories" to categories,
                "profileCompleted" to true
            )

            firestore.collection("users")
                .document(userId)
                .update(profileData as Map<String, Any>)
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Çıkış yap
    fun signOut() {
        auth.signOut()
    }

    // Test fonksiyonu (eski kod)
    suspend fun saveText(text: String): Result<String> {
        return try {
            val data = hashMapOf(
                "text" to text,
                "timestamp" to System.currentTimeMillis()
            )

            val documentRef = firestore.collection("test_collection")
                .add(data)
                .await()

            Result.success(documentRef.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
