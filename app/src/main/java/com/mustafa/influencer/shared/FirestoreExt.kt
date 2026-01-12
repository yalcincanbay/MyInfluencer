package com.mustafa.influencer.shared

import com.google.firebase.firestore.DocumentSnapshot

fun DocumentSnapshot.string(field: String): String =
    getString(field) ?: ""

fun DocumentSnapshot.bool(field: String): Boolean =
    getBoolean(field) ?: false

fun DocumentSnapshot.long(field: String): Long =
    when (val v = get(field)) {
        is Long -> v
        is Int -> v.toLong()
        is Double -> v.toLong()
        is Float -> v.toLong()
        is Number -> v.toLong()
        is String -> v.toLongOrNull() ?: 0L
        else -> 0L
    }

fun DocumentSnapshot.stringList(field: String): List<String> =
    (get(field) as? List<*>)?.mapNotNull { it as? String } ?: emptyList()

fun DocumentSnapshot.stringMap(field: String): Map<String, String> =
    (get(field) as? Map<*, *>)?.mapNotNull { (k, v) ->
        val kk = k as? String ?: return@mapNotNull null
        val vv = v as? String ?: return@mapNotNull null
        kk to vv
    }?.toMap() ?: emptyMap()
