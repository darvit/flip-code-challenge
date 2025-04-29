package com.flip.plugins

import com.flip.adapters.persistence.UserDbRepository
import com.flip.application.ports.persistence.UserRepository
import com.flip.adapters.persistence.UrlDbRepository
import com.flip.application.ports.persistence.UrlRepository
import org.koin.dsl.module

val infrastructureModule = module {
    single<UserRepository> { UserDbRepository() }
    single<UrlRepository> { UrlDbRepository() }
}