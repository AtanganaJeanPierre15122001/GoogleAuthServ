package com.example

import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

@Suppress("unused")
fun Application.module() {
    configureKoin()
    configureAuth()
    configureSerialization()
    configureMonitoring()
    configureRouting()
    configureSession()

}
