package com.mustafa.influencer.domain.repository

import com.mustafa.influencer.data.User
import com.mustafa.influencer.domain.model.UserType

interface UserRepository {
    suspend fun signUp(email: String, password: String, userType: UserType): String
    suspend fun signIn(email: String, password: String): String
    fun isLoggedIn(): Boolean
    fun currentUserId(): String?
    suspend fun getUser(userId: String): User

    // --- YENİ EKLENEN ---
    suspend fun getAllInfluencers(): List<User>

    suspend fun saveInfluencerProfile(
        userId: String,
        platforms: List<String>,
        platformLinks: Map<String, String>,
        categories: List<String>,
        bio: String
    )

    suspend fun saveAdvertiserProfile(
        userId: String,
        companyName: String,
        platforms: List<String>,
        categories: List<String>
    )

    fun signOut()
}