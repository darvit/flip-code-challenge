package com.flip.adapters.persistence

import com.flip.application.domain.Url
import com.flip.application.ports.persistence.UrlRepository
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.*
import com.flip.plugins.Database.dbExec
import java.util.UUID

object Urls : UUIDTable() {
    val url = varchar("url", length = 2048)
    val shortUrl = varchar("shorturl", length = 255)
    val userId = uuid("userid").nullable()
    val clicks = integer("clicks")
}

class UrlDbRepository : UrlRepository {
    override suspend fun add(url: Url) {
        dbExec {
            Urls.insert {
                it[id] = url.id
                it[Urls.url] = url.url
                it[Urls.shortUrl] = url.shortUrl
                it[Urls.userId] = url.userId
                it[Urls.clicks] = url.clicks
            }
        }
    }

    override suspend fun findByShortUrl(shortUrl: String): Url? {
        return dbExec {
            Urls.select { Urls.shortUrl eq shortUrl }
                .map { rowToUrl(it) }
                .singleOrNull()
        }
    }

    override suspend fun findById(id: UUID): Url? {
        return dbExec {
            Urls.select { Urls.id eq id }
                .map { rowToUrl(it) }
                .singleOrNull()
        }
    }

    override suspend fun findAllByUserId(userId: UUID): List<Url> {
        return dbExec {
            Urls.select { Urls.userId eq userId }
                .map { rowToUrl(it) }
        }
    }

    override suspend fun update(url: Url) {
        dbExec {
            Urls.update({ Urls.id eq url.id }) {
                it[Urls.url] = url.url
                it[Urls.shortUrl] = url.shortUrl
                it[Urls.clicks] = url.clicks
            }
        }
    }

    private fun rowToUrl(row: ResultRow): Url = Url(
        id = row[Urls.id].value,
        url = row[Urls.url],
        shortUrl = row[Urls.shortUrl],
        userId = row[Urls.userId],
        clicks = row[Urls.clicks]
    )
}
