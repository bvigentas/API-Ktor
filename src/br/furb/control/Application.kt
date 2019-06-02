package br.furb.ktorAPI.br.furb.model

import br.furb.dao.ComandaDAO
import br.furb.dao.UsuarioDAO
import br.furb.ktorAPI.br.furb.table.Comandas
import br.furb.model.Comanda
import br.furb.model.ComandaHolder
import com.google.gson.Gson
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.request.*
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.gson.*
import io.ktor.features.*
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import org.jetbrains.exposed.dao.EntityID
import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType

fun main (args: Array<String>) {
    embeddedServer(Netty, port = 8083) {

        val gson = Gson()

//        install(ContentNegotiation) {
//            gson {
//                setPrettyPrinting()
//            }
//        }

        val usuarioDao = UsuarioDAO()
        val comandaDao = ComandaDAO()

        routing {

            get("/usuarios") {
                try {
                    val usuarios = usuarioDao.getUsuarios()
                    call.respond(gson.toJson(usuarios))
                } catch (e: Exception) {

                }
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
                try {
                    val comanda = comandaDao.getComanda(call.parameters["id"]!!.toInt())
                    call.respond(gson.toJson(comanda))
                } catch (e: Exception) {

                }
            }

            post("/comandas") {
                try {
                    val post = call.receive<ComandaHolder>()

//                    comandaDao.createComanda(post)
                    call.respond(post)
                } catch (e: Exception) {
//                    call.respond(e)
                    e.printStackTrace()
                }

            }

            put("comandas/{id}") {
                
            }

            delete("/comandas/{id}"){
                try {
                    comandaDao.deleteComanda(call.parameters["id"]!!.toInt())
                    call.respond(mapOf(true to true))
                } catch (e: Exception) {
                    call.respondText(e.toString())
                }

            }

        }
    }.start(wait = true)
}

