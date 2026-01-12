package com.mustafa.influencer.shared

import com.mustafa.influencer.data.repository.UserRepositoryImpl
import com.mustafa.influencer.domain.repository.UserRepository

class AppSession(
    private val repo: UserRepository = UserRepositoryImpl()
) {
    fun signOut() = repo.signOut()
}
