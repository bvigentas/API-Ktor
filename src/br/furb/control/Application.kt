package br.furb.ktorAPI.br.furb.model

import br.furb.dao.ComandaDAO
import br.furb.dao.UsuarioDAO
import br.furb.model.*
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

fun main (args: Array<String>) {
    embeddedServer(Netty, port = 8083) {

        val gson = Gson()

       install(ContentNegotiation) {
           gson {
               setPrettyPrinting()
           }
       }

        val usuarioDao = UsuarioDAO()
        val comandaDao = ComandaDAO()

        routing {

            get("/usuarios") {
                try {
                    val usuarios = usuarioDao.getUsuarios().map { UsuarioJson(it) }
                    call.respond(usuarios)
                } catch (e: Exception) {
                }
            }

            get("/usuarios/{id}") {

                try {
                    // val usuario = usuarioDao.getUsuarios().map { UsuarioJson(it) }.get(call.parameters["id"]!!.toInt() - 1)
                    val usuario = usuarioDao.getUsuario(call.parameters["id"]!!.toInt())
                    call.respond(gson.toJson(usuario))
                }catch (e: Exception){
                    call.respond(e)
                }
            }

            post("/usuarios") {
                try {
                    var usuario = call.receive<UsuarioJson>()
                    usuario = usuarioDao.createUsuario(usuario.email, usuario.senha)
                    call.respond(usuario)
                } catch (e: Exception) {
                    call.respond(e)
                }
            }

            put("/usuarios/{id}") {
                try {
                    var parameters = call.receive<UsuarioJson>()
                    val usuario = usuarioDao.updateUsuario(call.parameters["id"]!!.toInt(), parameters.email, parameters.senha)

                    call.respond(usuario)

                } catch (e: Exception) {
                    call.respondText(e.toString())
                }
            }

            delete("/usuarios/{id}") {
                try {
                    usuarioDao.deleteUsuario(call.parameters["id"]!!.toInt())
                    call.respond(mapOf(true to true))
                } catch (e: Exception) {
                    call.respondText(e.toString())
                }
            }

            delete("/usuarios") {
                try {
                    usuarioDao.deleteUsuarios()
                    call.respond(mapOf(true to true))
                } catch (e: Exception) {
                    call.respondText(e.toString())
                }
            }

            get("/comandas") {
                try{
                    val comandas = comandaDao.getComandas().map { ComandaJson(it) }
                    call.respond(comandas)
                }catch (e: Exception) {
                    call.respond(e)
                }
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
                    var comanda = call.receive<ComandaJson>()
                    comanda = comandaDao.createComanda(comanda.idUsuario, comanda.produtos, comanda.valorTotal)
                    call.respond(comanda)
                } catch (e: Exception) {
//                    call.respond(e)
                    e.printStackTrace()
                }
            }

            put("comandas/{id}") {
                try {
                    var parameters = call.receive<ComandaJson>()
                    val comanda = comandaDao.updateComanda(call.parameters["id"]!!.toInt(), parameters.produtos, parameters.valorTotal)

                    // mudar a saida aqui - colocar string e to json

                    call.respond(comanda)

                } catch (e: Exception) {
                    call.respondText(e.toString())
                }
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

