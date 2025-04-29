package com.flip.application.ports.persistence

import com.flip.application.domain.User
import java.util.UUID

interface UserRepository {
    suspend fun add(user: User)
    suspend fun findById(id: UUID): User?
    suspend fun findAll(): List<User>
}
