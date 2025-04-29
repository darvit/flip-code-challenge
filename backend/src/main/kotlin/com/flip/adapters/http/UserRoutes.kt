package com.flip.adapters.http

import com.trendyol.kediatr.Mediator
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import java.util.*
import kotlinx.serialization.Serializable
import com.flip.application.usecases.users.CreateUserUsecase
import com.flip.application.usecases.users.ListUsersUsecase
import com.flip.application.usecases.users.CreateUserCommmand
import com.flip.adapters.serializers.UUIDSerializer

fun Route.userRoute() {
    val createUserUsecase by inject<CreateUserUsecase>()
    val listUsersUsecase by inject<ListUsersUsecase>()

    get("users") {
        val users = listUsersUsecase.invoke()
        call.respond(users.map { UserDto(it.id, it.username) })
    }

    post("users") {
        val request = call.receive<AddUserRequest>()
        createUserUsecase.invoke(CreateUserCommmand(request.username))
        call.respond(HttpStatusCode.Created)
    }
}

@Serializable
data class AddUserRequest(
    val username: String
)

@Serializable
data class UserDto(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    val username: String
)
