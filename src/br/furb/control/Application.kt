package br.furb.ktorAPI.br.furb.model

import br.furb.dao.ComandaDAO
import br.furb.dao.UsuarioDAO
import br.furb.model.Comanda
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.request.*
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.gson.*
import io.ktor.features.*
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType

fun main (args: Array<String>) {
    embeddedServer(Netty, port = 8083) {

        install(ContentNegotiation) {
            gson {
                setPrettyPrinting()
            }
        }

        val usuarioDao = UsuarioDAO()
        val comandaDao = ComandaDAO()

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

            get("/comandas/{id}") {

            }

            post("/comandas") {
                val post = call.receive<Comanda>()
                call.respond(post)
            }

            put("comandas/{id}") {
                
            }

            delete("/comandas/{id}"){
                comandaDao.deleteComanda(call.parameters["id"]!!.toInt())
                call.respond(mapOf(true to true))
            }

        }
    }.start(wait = true)
}

