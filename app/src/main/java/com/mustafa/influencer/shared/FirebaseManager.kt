package com.mustafa.influencer.shared

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

object FirebaseManager {

    private val firestore = FirebaseFirestore.getInstance()

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
