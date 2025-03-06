package com.example.routes

import com.example.model.User
import com.example.model.theUser
import com.example.repository.UserRepository
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.userRoutes() {
    val userRepository by inject<UserRepository>()

    route("/users") {
        get {
            call.respond(userRepository.getAllUsers())
        }

        get("/{id}") {
            val id = call.parameters["id"] ?: return@get call.respondText("Missing ID", status = io.ktor.http.HttpStatusCode.BadRequest)
            val user = userRepository.getUserById(id)
            if (user != null) call.respond(user) else call.respondText("User Not Found", status = io.ktor.http.HttpStatusCode.NotFound)
        }

        post {
            val user = call.receive<theUser>()
            if (userRepository.createUser(user)) {
                call.respondText("User created", status = io.ktor.http.HttpStatusCode.Created)
            } else {
                call.respondText("Error creating user", status = io.ktor.http.HttpStatusCode.InternalServerError)
            }
        }

        put("/{id}") {
            val id = call.parameters["id"] ?: return@put call.respondText("Missing ID", status = io.ktor.http.HttpStatusCode.BadRequest)
            val user = call.receive<theUser>()
            if (userRepository.updateUser(id, user)) {
                call.respondText("User updated", status = io.ktor.http.HttpStatusCode.OK)
            } else {
                call.respondText("User not found", status = io.ktor.http.HttpStatusCode.NotFound)
            }
        }

        delete("/{id}") {
            val id = call.parameters["id"] ?: return@delete call.respondText("Missing ID", status = io.ktor.http.HttpStatusCode.BadRequest)
            if (userRepository.deleteUser(id)) {
                call.respondText("User deleted", status = io.ktor.http.HttpStatusCode.OK)
            } else {
                call.respondText("User not found", status = io.ktor.http.HttpStatusCode.NotFound)
            }
        }
    }
}
