package com.mustafa.influencer.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.mustafa.influencer.data.User
import com.mustafa.influencer.data.remote.UserDataSource
import com.mustafa.influencer.domain.model.UserType
import com.mustafa.influencer.domain.repository.UserRepository
import kotlinx.coroutines.tasks.await

class UserRepositoryImpl(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val ds: UserDataSource = UserDataSource()
) : UserRepository {

    override suspend fun signUp(email: String, password: String, userType: UserType): String {
        val res = auth.createUserWithEmailAndPassword(email, password).await()
        val uid = res.user?.uid ?: error("User ID alınamadı")
        ds.createMinimalUser(uid, email, userType)
        return uid
    }

    override suspend fun signIn(email: String, password: String): String {
        val res = auth.signInWithEmailAndPassword(email, password).await()
        return res.user?.uid ?: error("User ID alınamadı")
    }

    override fun isLoggedIn(): Boolean = auth.currentUser != null

    override fun currentUserId(): String? = auth.currentUser?.uid

    override suspend fun getUser(userId: String): User = ds.getUser(userId)

    // --- YENİ EKLENEN IMPLEMENTASYON ---
    override suspend fun getAllInfluencers(): List<User> {
        return ds.getAllInfluencers()
    }

    override suspend fun saveInfluencerProfile(
        userId: String,
        platforms: List<String>,
        platformLinks: Map<String, String>,
        categories: List<String>,
        bio: String
    ) {
        ds.updateInfluencerProfile(
            userId = userId,
            platforms = platforms,
            platformLinks = platformLinks,
            categories = categories,
            bio = bio
        )
    }

    override suspend fun saveAdvertiserProfile(
        userId: String,
        companyName: String,
        platforms: List<String>,
        categories: List<String>
    ) {
        ds.updateAdvertiserProfile(
            userId = userId,
            companyName = companyName,
            platforms = platforms,
            categories = categories
        )
    }

    override fun signOut() {
        auth.signOut()
    }
}