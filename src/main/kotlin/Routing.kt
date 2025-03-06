package com.example

import com.example.repository.UserDataSource
import com.example.routes.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.calllogging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import kotlinx.serialization.Serializable
import org.koin.java.KoinJavaComponent.inject
import org.koin.ktor.*
import org.slf4j.event.*

fun Application.configureRouting() {
    routing {
        val userDataSource: UserDataSource by inject(UserDataSource::class.java)
        UnauthorizedRoute()
        deleteUserRoute(application,userDataSource)
        signOutRoute()
        //userRoutes() route pour tester mongo
        getUserInfoRoute(application,userDataSource)
        updateUserInfoRoute(application,userDataSource)
        authorizedRoute()
        tokenVerificationRoute(application, userDataSource)
    }
}
