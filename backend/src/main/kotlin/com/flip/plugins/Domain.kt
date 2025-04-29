package com.flip.plugins

import com.flip.application.usecases.users.CreateUserUsecase
import com.flip.application.usecases.users.ListUsersUsecase
import com.flip.application.usecases.urls.CreateUrlUsecase
import com.flip.application.usecases.urls.CreateRandomUrlUsecase
import com.flip.application.usecases.urls.GetUrlsByUserUsercase
import com.flip.application.usecases.urls.GetUrlUsecase
import com.typesafe.config.Config
import org.koin.dsl.module

val domainModule = module {
    single<CreateUserUsecase> { CreateUserUsecase(get()) }
    single<ListUsersUsecase> { ListUsersUsecase(get()) }
    single<CreateUrlUsecase> { CreateUrlUsecase(get()) }
    single<CreateRandomUrlUsecase> { CreateRandomUrlUsecase(get()) }
    single<GetUrlsByUserUsercase> { GetUrlsByUserUsercase(get()) }
    single<GetUrlUsecase> { GetUrlUsecase(get()) }
}