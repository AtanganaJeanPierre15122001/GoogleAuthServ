package com.example.routes

import com.example.model.Endpoint
import com.example.model.UserSession
import com.example.models.ApiResponse
import com.example.repository.UserDataSource
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.getUserInfoRoute(
    app : Application,
    userDataSource: UserDataSource
){
    authenticate("auth-session") {
        get(Endpoint.GetUserInfo.path){
            val userSession = call.principal<UserSession>()
            if (userSession == null){
                app.log.info("INVALID SESSION")
                call.respondRedirect(Endpoint.Unauthorized.path)
            }else{
                try {
                    call.respond(
                        message = ApiResponse(
                            success = true,
                            user = userDataSource.getUserInfo(userId = userSession.id)
                        ),
                        status = HttpStatusCode.OK
                    )
                }catch (e: Exception){
                    app.log.info("Getting User Info ERROR : ${e.message}")
                    call.respondRedirect(Endpoint.Unauthorized.path)
                }
            }
        }
    }
}