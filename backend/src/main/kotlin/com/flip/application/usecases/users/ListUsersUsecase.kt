package com.flip.application.usecases.users

import com.flip.application.domain.User
import com.flip.application.ports.persistence.UserRepository
import io.klogging.logger

class ListUsersUsecase(
    private val userRepository: UserRepository
) {
    private val logger = logger<ListUsersUsecase>()

    suspend fun invoke(): List<User> {
        try {
            return userRepository.findAll()
        } catch (e: Exception) {
            logger.error("Error listing users", e)
            throw e
        }
    }
}
