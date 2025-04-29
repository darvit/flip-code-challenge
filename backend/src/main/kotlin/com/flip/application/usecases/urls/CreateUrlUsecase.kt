package com.flip.application.usecases.urls

import com.flip.application.domain.Url
import com.flip.application.ports.persistence.UrlRepository
import java.util.UUID
import io.klogging.logger

class CreateUrlUsecase(
    private val urlRepository: UrlRepository
) {
    private val logger = logger<CreateUrlUsecase>()

    suspend fun invoke(command: CreateUrlCommand): Url {
        try {
            if (command.shortUrl.isEmpty()) {
                throw IllegalArgumentException("Short URL cannot be empty")
            }

            val existingUrl = urlRepository.findByShortUrl(command.shortUrl)
            if (existingUrl != null) {
                throw IllegalStateException("Short URL already exists")
            }

            val url = Url.from(command.url, command.shortUrl, command.userId)
            urlRepository.add(url)
            return url
        } catch (e: Exception) {
            logger.error("Error creating url", e)
            throw e
        }
    }
}

data class CreateUrlCommand(
    val url: String,
    val shortUrl: String,
    val userId: UUID?,
)