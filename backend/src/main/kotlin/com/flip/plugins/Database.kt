package com.flip.plugins

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.config.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.LoggerFactory
import javax.sql.DataSource

object Database {
    private val log = LoggerFactory.getLogger(this::class.java)

    fun connectAndMigrate(config: ApplicationConfig) {
        log.info("Initialising database")
        val pool = hikari(config)
        Database.connect(pool)

        val isDevelopment = config.property("ktor.development").getString().toBoolean()
        if (isDevelopment) {
            runFlyway(pool)
        }
    }

    private fun hikari(appConfig: ApplicationConfig): HikariDataSource {
        val config = HikariConfig().apply {
            driverClassName = appConfig.property("storage.driverClassName").getString()
            jdbcUrl = appConfig.property("storage.jdbcURL").getString()
            username = appConfig.property("storage.username").getString()
            password = appConfig.property("storage.password").getString()
            maximumPoolSize = 3
            isAutoCommit = false
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"
            validate()
        }

        return HikariDataSource(config)
    }

    private fun runFlyway(datasource: DataSource) {
        val flyway = Flyway.configure().dataSource(datasource).load()
        try {
            flyway.info()
            flyway.migrate()
        } catch (e: Exception) {
            log.error("Exception running flyway db.migration", e)
            throw e
        }
        log.info("Flyway db.migration has finished")
    }

    suspend fun <T> dbExec(
        block: () -> T
    ): T = withContext(Dispatchers.IO) {
        transaction { block() }
    }
}