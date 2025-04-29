package com.flip.application.usecases.urls

import com.flip.application.ports.persistence.UrlRepository
import com.flip.application.domain.Url
import io.klogging.logger

class GetUrlUsecase(
    private val urlRepository: UrlRepository,
) {
    private val logger = logger<GetUrlUsecase>()
    private val notFoundRedirect = "http://localhost:3000/not-found"
    
    suspend fun invoke(command: GetUrlCommand): String {
        try {
            val url = urlRepository.findByShortUrl(command.shortUrl)
            if (url == null) {
                logger.info("URL not found, redirecting to: $notFoundRedirect")
                return notFoundRedirect
            }

            val updatedUrl = url.incrementClicks()
            urlRepository.update(updatedUrl)
            logger.info("URL clicked: ${updatedUrl.url}")

            return url.url
        } catch (e: Exception) {
            logger.error("Error getting URL: ${e.message}")
            throw e
        }
    }
}

data class GetUrlCommand(
    val shortUrl: String
)
