package com.example.routes

import com.example.model.Endpoint
import com.example.model.UserSession
import com.example.models.ApiResponse
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*

fun Route.signOutRoute(){
    authenticate("auth-session") {
        get(Endpoint.SignOut.path){
            call.sessions.clear<UserSession>()
            call.respond(
                message = ApiResponse(success = true),
                status = HttpStatusCode.OK
            )
        }
    }
}