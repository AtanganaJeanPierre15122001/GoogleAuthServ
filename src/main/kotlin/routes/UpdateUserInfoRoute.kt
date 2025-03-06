package com.example.routes

import com.example.model.Endpoint
import com.example.model.UserSession
import com.example.model.UserUpdate
import com.example.models.ApiResponse
import com.example.repository.UserDataSource
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.updateUserInfoRoute(
    app : Application,
    userDataSource: UserDataSource
){
    authenticate("auth-session") {
        put(Endpoint.UpdateUserInfo.path){
            val userSession = call.principal<UserSession>()
            val userUpdate = call.receive<UserUpdate>()
            if (userSession == null) {
                app.log.info("INVALID SESSION")
                call.respondRedirect(Endpoint.Unauthorized.path)
            }else{
                try {
                    val response = userDataSource.updateUserInfo(
                        userId = userSession.id,
                        firstname = userUpdate.firstName,
                        lastname = userUpdate.lastName
                    )
                    if (response){
                        app.log.info("USER SUCCEFULY UPDATED")
                        call.respond(
                            message = ApiResponse(
                                success = true,
                                message = "Succesfuly Update"
                            ),
                            status = HttpStatusCode.OK
                        )
                    }else{
                        app.log.info("ERROR UPDATING THE USER")
                        call.respond(
                            message = ApiResponse(success = false),
                            status = HttpStatusCode.BadRequest
                        )
                    }

                }catch (e : Exception){
                    app.log.info("UPDATE USER INFO ERROR  : $e")
                    call.respondRedirect(Endpoint.Unauthorized.path)
                }
            }
        }
    }
}