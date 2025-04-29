package com.flip.application.domain

import java.util.UUID

data class Url(
    val id: UUID,
    val url: String,
    val shortUrl: String,
    val userId: UUID?,
    val clicks: Int,
) {
    fun incrementClicks(): Url {
        return copy(clicks = clicks + 1)
    }

    companion object {
        fun from(url: String, shortUrl: String, userId: UUID?): Url {
            require(url.isValidUrl()) { 
                throw IllegalArgumentException("Invalid URL")
            }

            return Url(UUID.randomUUID(), url, shortUrl, userId, 0)
        }

        private fun String.isValidUrl(): Boolean {
            return this.startsWith("http://") || this.startsWith("https://")
        }
    }
}
