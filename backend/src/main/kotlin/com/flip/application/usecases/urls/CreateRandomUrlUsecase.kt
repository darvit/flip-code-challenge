package com.flip.application.usecases.urls

import com.flip.application.domain.Url
import com.flip.application.ports.persistence.UrlRepository
import java.util.UUID
import io.klogging.logger

class CreateRandomUrlUsecase(
    private val urlRepository: UrlRepository
) {
    private val logger = logger<CreateRandomUrlUsecase>()
    
    suspend fun invoke(command: CreateRandomUrlCommand): Url {
        try {
            val shortUrl = generateShortUrl(command.userId)
            val url = Url.from(command.url, shortUrl, command.userId)
            urlRepository.add(url)
            return url
        } catch (e: Exception) {
            logger.error("Error creating random short url", e)
            throw e
        }
    }

    private fun generateShortUrl(userId: UUID?): String {
        return userId?.toString() + UUID.randomUUID().toString().substring(0, 8)
    }
}

data class CreateRandomUrlCommand(
    val url: String,
    val userId: UUID?,
)