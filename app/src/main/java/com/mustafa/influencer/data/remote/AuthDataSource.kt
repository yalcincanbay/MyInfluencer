package com.mustafa.influencer.data.remote

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

class AuthDataSource(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) {
    fun isUserLoggedIn(): Boolean = auth.currentUser != null
    fun currentUserId(): String? = auth.currentUser?.uid

    suspend fun signUp(email: String, password: String): String {
        val result = auth.createUserWithEmailAndPassword(email, password).await()
        return result.user?.uid ?: error("User ID alınamadı")
    }

    suspend fun signIn(email: String, password: String): String {
        val result = auth.signInWithEmailAndPassword(email, password).await()
        return result.user?.uid ?: error("User ID alınamadı")
    }

    fun signOut() = auth.signOut()
}
