package com.flip.application.usecases.urls

import com.flip.application.domain.Url
import com.flip.application.ports.persistence.UrlRepository
import java.util.UUID
import io.klogging.logger

class GetUrlsByUserUsercase(
    private val urlRepository: UrlRepository
) {
    private val logger = logger<GetUrlsByUserUsercase>()

    suspend fun invoke(command: GetUrlsByUserCommand): List<Url> {
        try {
            return urlRepository.findAllByUserId(command.userId)
        } catch (e: Exception) {
            logger.error("Error getting urls by user", e)
            throw e
        }
    }
}

data class GetUrlsByUserCommand(
    val userId: UUID
)
