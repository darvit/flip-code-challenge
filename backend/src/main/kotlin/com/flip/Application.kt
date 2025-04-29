package com.flip

import com.flip.plugins.*
import io.klogging.Level
import io.klogging.config.loggingConfiguration
import io.klogging.config.seq
import io.klogging.rendering.RENDER_SIMPLE
import io.klogging.sending.STDOUT
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.plugins.defaultheaders.*
import io.ktor.server.plugins.requestvalidation.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import kotlinx.serialization.json.Json
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun main(args: Array<String>): Unit = EngineMain.main(args)

fun Application.module() {
    val config = environment.config;
    loggingConfiguration {
        sink("stdout", RENDER_SIMPLE, STDOUT)
        sink("seq", seq(config.property("logging.seqUrl").getString()))
        logging {
            fromLoggerBase("com.flip")
            fromMinLevel(Level.DEBUG) {
                toSink("stdout")
                toSink("seq")
            }
        }
    }

    install(DefaultHeaders)

    install(RequestValidation)

    install(StatusPages) {
        exception<RequestValidationException> { call, cause ->
            call.respond(HttpStatusCode.BadRequest, cause.reasons.joinToString())
        }
    }

    install(CallLogging)

    install(Koin) {
        slf4jLogger()
        modules(domainModule)
        modules(infrastructureModule)
    }

    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
        })
    }

    install(CORS) {
        anyHost()
        allowHeader(HttpHeaders.ContentType)
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Post)
        allowMethod(HttpMethod.Delete)
    }

    Database.connectAndMigrate(config)

    configureRouting()
}
