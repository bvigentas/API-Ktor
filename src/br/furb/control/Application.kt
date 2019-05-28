package br.furb.ktorAPI.br.furb.model

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.request.*
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.gson.*
import io.ktor.features.*
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

fun main (args: Array<String>) {
    embeddedServer(Netty, port = 8081) {

        routing {

            get("/usuarios") {
                call.respondText("Teste")
            }

            get("/usuarios/{id}") {
                call.respondText("Teste1")
            }

            get("/comandas") {
                call.respondText("Teste2")
            }

            post("/usuarios") {
                call.respondText("Teste3")
            }

            put("/usuarios/{id}") {
                call.respondText("Teste4")
            }

            delete("/usuarios/{id}") {
                call.respondText("Teste5")
            }

            delete("/usuarios") {
                call.respondText("Teste6")
            }

        }
    }.start(wait = true)
}

