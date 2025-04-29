package com.flip.application.domain

import java.util.UUID

data class User(
    val id: UUID,
    val username: String,
) {
    companion object {
        fun from(username: String): User {
            return User(UUID.randomUUID(), username)
        }
    }
}
