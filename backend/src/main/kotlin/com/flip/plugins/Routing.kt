package com.flip.plugins

import com.flip.adapters.http.userRoute
import com.flip.adapters.http.urlRoute
import io.ktor.server.application.*
import io.ktor.server.plugins.swagger.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        swaggerUI(path = "swagger", swaggerFile = "openapi/documentation.yaml")

        userRoute()
        urlRoute()
    }
}
