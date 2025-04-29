package com.flip.application.ports.persistence

import com.flip.application.domain.Url
import java.util.UUID

interface UrlRepository {
    suspend fun add(url: Url)
    suspend fun findByShortUrl(shortUrl: String): Url?
    suspend fun findById(id: UUID): Url?
    suspend fun findAllByUserId(userId: UUID): List<Url>
    suspend fun update(url: Url)
}
