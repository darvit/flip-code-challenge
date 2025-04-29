package com.flip.adapters.persistence

import com.flip.application.domain.User
import com.flip.application.ports.persistence.UserRepository
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.*
import com.flip.plugins.Database.dbExec
import java.util.UUID

object Users: UUIDTable() {
    val username: Column<String> = varchar("username", 150)
}

class UserDbRepository: UserRepository {
    override suspend fun add(user: User) {
        dbExec {
            Users.insert {
                it[id] = user.id
                it[username] = user.username
            }
        }
    }

    override suspend fun findById(id: UUID): User? {
        return dbExec {
            Users
                .selectAll()
                .where { Users.id eq id }
                .map { toUser(it) }
                .singleOrNull()
        }
    }

    override suspend fun findAll(): List<User> {
        return dbExec {
            Users
                .selectAll()
                .orderBy(Users.username)
                .map { toUser(it) }
        }
    }

    private fun toUser(row: ResultRow): User =
        User(row[Users.id].value, row[Users.username])
}
