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
import io.ktor.server.sessions.*

fun Route.deleteUserRoute(
    app : Application,
    userDataSource: UserDataSource
){
    authenticate ("auth-session"){
        delete(Endpoint.DeleteUser.path){
            val userSession = call.principal<UserSession>()
            if (userSession == null) {
                app.log.info("INVALID SESSION")
                call.respondRedirect(Endpoint.Unauthorized.path)
            }else{
                try {
                    call.sessions.clear<UserSession>()
                    val result = userDataSource.deleteUser(userId = userSession.id)
                    if (result){
                        app.log.info("USER SUCCEFULY DELETED")
                        call.respond(
                            message = ApiResponse(success = true),
                            status = HttpStatusCode.OK
                        )
                    }else{
                        app.log.info("ERROR DELETING THE USER")
                        call.respond(
                            message = ApiResponse(success = false),
                            status = HttpStatusCode.BadRequest
                        )
                    }

                }catch (e : Exception){
                    app.log.info("DELETING USER INFO ERROR  : $e")
                    call.respondRedirect(Endpoint.Unauthorized.path)
                }
            }
        }
    }
}