package com.example.routes

import com.example.model.Endpoint
import com.example.models.ApiResponse
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*
import io.ktor.server.response.*

fun Route.authorizedRoute(){
    authenticate("auth-session") {
        get(Endpoint.Authorized.path){
            call.respond(
                message = ApiResponse(success = true),
                status = HttpStatusCode.OK
            )
        }
    }
}