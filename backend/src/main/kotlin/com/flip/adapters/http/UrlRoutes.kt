package com.flip.adapters.http

import com.flip.application.usecases.urls.CreateUrlUsecase
import com.flip.application.usecases.urls.CreateRandomUrlUsecase
import com.flip.application.usecases.urls.CreateUrlCommand
import com.flip.application.usecases.urls.CreateRandomUrlCommand
import com.flip.application.usecases.urls.GetUrlsByUserCommand
import com.flip.application.usecases.urls.GetUrlsByUserUsercase
import com.flip.application.usecases.urls.GetUrlCommand
import com.flip.application.usecases.urls.GetUrlUsecase
import com.flip.application.domain.Url
import com.flip.adapters.serializers.UUIDSerializer
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import java.util.*
import kotlinx.serialization.Serializable

fun Route.urlRoute() {
    val createUrlUsecase by inject<CreateUrlUsecase>()
    val createRandomUrlUsecase by inject<CreateRandomUrlUsecase>()
    val getUrlsByUserUsecase by inject<GetUrlsByUserUsercase>()
    val getUrlUsecase by inject<GetUrlUsecase>()

    post("urls") {
        val request = call.receive<CreateUrlRequest>()
        val url = createUrlUsecase.invoke(CreateUrlCommand(request.url, request.shortUrl, request.userId))
        call.respond(HttpStatusCode.Created, UrlDto.from(url))
    }
    
    post("urls/random") {
        val request = call.receive<CreateRandomUrlRequest>()
        val url = createRandomUrlUsecase.invoke(CreateRandomUrlCommand(request.url, request.userId))
        call.respond(HttpStatusCode.Created, UrlDto.from(url))
    }

    get("urls/users/{userId}") {
        val userId = call.parameters["userId"]?.let { UUID.fromString(it) }
        if (userId == null) {
            call.respond(HttpStatusCode.BadRequest)
            return@get
        }

        val urls = getUrlsByUserUsecase.invoke(GetUrlsByUserCommand(userId))
        call.respond(urls.map { UrlDto.from(it) })
    }

    get("{shortUrl}") {
        try {
            val shortUrl = call.parameters["shortUrl"]
            if (shortUrl == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }
            val url = getUrlUsecase.invoke(GetUrlCommand(shortUrl))
            call.respondRedirect(url)
        } catch (e: Exception) {
            println(e)
            call.respond(HttpStatusCode.NotFound)
        }
    }
}

@Serializable
data class CreateUrlRequest(
    val url: String,
    val shortUrl: String,
    @Serializable(with = UUIDSerializer::class)
    val userId: UUID?
)

@Serializable
data class CreateRandomUrlRequest(
    val url: String,
    @Serializable(with = UUIDSerializer::class)
    val userId: UUID?
)

@Serializable
data class UrlDto(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    val url: String,
    val shortUrl: String,
    val clicks: Int
) {
    companion object {
        fun from(url: Url): UrlDto {
            return UrlDto(url.id, url.url, url.shortUrl, url.clicks)
        }
    }
}