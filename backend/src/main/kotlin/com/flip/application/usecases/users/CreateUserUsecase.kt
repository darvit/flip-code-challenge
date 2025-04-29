package com.flip.application.usecases.users

import com.flip.application.domain.User
import com.flip.application.ports.persistence.UserRepository
import io.klogging.logger

class CreateUserUsecase(
    private val userRepository: UserRepository
) {
    private val logger = logger<CreateUserUsecase>()

    suspend fun invoke(command: CreateUserCommmand): User {
        try {
            val user = User.from(command.username)
            userRepository.add(user)
            logger.info("User created: $user")
            return user
        } catch (e: Exception) {
            logger.error("Error creating user", e)
            throw e
        }
    }
}

data class CreateUserCommmand(
    val username: String
)