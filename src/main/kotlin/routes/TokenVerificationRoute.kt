package com.example.routes

import com.example.model.ApiRequest
import com.example.model.Endpoint
import com.example.model.User
import com.example.model.UserSession
import com.example.repository.UserDataSource
import com.example.util.Constants.AUDIENCE
import com.example.util.Constants.ISSUER
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken
import com.google.api.client.auth.*
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import com.google.api.client.googleapis.auth.oauth2.*
import com.google.api.client.json.gson.GsonFactory
import com.google.api.client.http.javanet.NetHttpTransport
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import io.ktor.util.pipeline.*

fun Route.tokenVerificationRoute(
    app: Application,
    userDataSource: UserDataSource
) {
    post(Endpoint.TokenVerification.path) {
        val request = call.receive<ApiRequest>()
        if (request.tokenId.isNotEmpty()) {
            val result = verifyGoogleTokenId(tokenId = request.tokenId)
            if (result != null) {
                val name = result.payload["name"].toString()
                val email = result.payload["email"].toString()
                val sub = result.payload["sub"].toString()
                val profilePhoto = result.payload["picture"].toString()

                val user = User(
                    id = sub,
                    name = name,
                    emailAdress = email,
                    profilePhoto = profilePhoto,
                )

                val response = userDataSource.saveUserInfo(user = user)
                if (response) {
                    app.log.info("USER SUCCESSFULLY SAVE/RETRIEVED")
                    call.sessions.set(UserSession(id = sub, name = name)) // Utilisez l'ID et le nom de l'utilisateur
                    call.respondRedirect(Endpoint.Authorized.path)
                } else {
                    app.log.info("ERROR SAVING THE USER")
                    call.respondRedirect(Endpoint.Unauthorized.path)
                }
            } else {
                app.log.info("TOKEN VERIFICATION FAILED")
                call.respondRedirect(Endpoint.Unauthorized.path)
            }
        } else {
            app.log.info("EMPTY TOKEN ID")
            call.respondRedirect(Endpoint.Unauthorized.path)
        }
    }
}

private suspend fun PipelineContext<Unit, ApplicationCall>.saveUserToDatabase(
    app: Application,
    result: GoogleIdToken,
    userDataSource: UserDataSource
) {
    val name = result.payload["name"].toString()
    val email = result.payload["email"].toString()
    val sub = result.payload["sub"].toString()
    val profilePhoto = result.payload["picture"].toString()

    val user = User(
        id = sub,
        name = name,
        emailAdress = email,
        profilePhoto = profilePhoto,
    )

    val response = userDataSource.saveUserInfo(user = user)
    if (response) {
        app.log.info("USER SUCCESSFULLY SAVE/RETRIEVED")
        call.sessions.set(UserSession(id = sub, name = name)) // Utilisez l'ID et le nom de l'utilisateur
        call.respondRedirect(Endpoint.Authorized.path)
    } else {
        app.log.info("ERROR SAVING THE USER")
        call.respondRedirect(Endpoint.Unauthorized.path)
    }
}

fun verifyGoogleTokenId(tokenId: String): GoogleIdToken? {
    return try {
        val verifier = GoogleIdTokenVerifier.Builder(NetHttpTransport(), GsonFactory())
            .setAudience(listOf(AUDIENCE))
            .setIssuer(ISSUER)
            .build()
        verifier.verify(tokenId)
    } catch (e: Exception) {
        null
    }
}