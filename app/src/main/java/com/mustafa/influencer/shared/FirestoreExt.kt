package com.mustafa.influencer.shared

import com.google.firebase.firestore.DocumentSnapshot

fun DocumentSnapshot.string(field: String): String =
    getString(field) ?: ""

fun DocumentSnapshot.bool(field: String): Boolean =
    getBoolean(field) ?: false

fun DocumentSnapshot.stringList(field: String): List<String> =
    (get(field) as? List<*>)?.filterIsInstance<String>() ?: emptyList()

fun DocumentSnapshot.stringMap(field: String): Map<String, String> =
    (get(field) as? Map<*, *>)?.mapNotNull { (k, v) ->
        val key = k as? String ?: return@mapNotNull null
        val value = v as? String ?: return@mapNotNull null
        key to value
    }?.toMap() ?: emptyMap()
