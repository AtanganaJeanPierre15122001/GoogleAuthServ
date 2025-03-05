package com.example

import com.example.model.Endpoint
import com.example.model.UserSession
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*

fun Application.configureAuth(){
    install(Authentication){
         session<UserSession>(name = "auth-session"){
            validate { session->
                session
            }
             challenge{
                 call.respondRedirect(Endpoint.Authorized.path)
             }
         }
    }
}