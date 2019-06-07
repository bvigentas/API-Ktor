package br.furb.control.usuario

import br.furb.daoImpl.UsuarioDAO
import br.furb.model.UsuarioJson
import io.ktor.application.call
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.*


val usuarioDao = UsuarioDAO()

fun Route.usuario() {
    get("/usuarios") {
        try {
            val usuarios = usuarioDao.listar().map { UsuarioJson(it) }
            call.respond(usuarios)
        } catch (e: Exception) {
        }
    }
    get("/usuarios/{id}") {

        try {
            // val usuario = usuarioDao.getUsuarios().map { UsuarioJson(it) }.get(call.parameters["id"]!!.toInt() - 1)
            val usuario = usuarioDao.getUnique(call.parameters["id"]!!.toInt())
            call.respond(usuario)
        } catch (e: Exception) {
            call.respond(e)
        }
    }
    post("/usuarios") {
        try {
            var usuario = call.receive<UsuarioJson>()
            usuario = usuarioDao.create(usuario)
            call.respond(usuario)
        } catch (e: Exception) {
            call.respond(e)
        }
    }
    put("/usuarios/{id}") {
        try {
            var usuario = call.receive<UsuarioJson>()
            usuario = usuarioDao.update(
                call.parameters["id"]!!.toInt(),
                usuario
            )

            call.respond(usuario)

        } catch (e: Exception) {
            call.respondText(e.toString())
            e.printStackTrace()
        }
    }
    delete("/usuarios/{id}") {
        try {
            usuarioDao.delete(call.parameters["id"]!!.toInt())
            call.respond(mapOf(true to true))
        } catch (e: Exception) {
            call.respondText(e.toString())
        }
    }
    delete("/usuarios") {
        try {
            usuarioDao.deleteAll()
            call.respond(mapOf(true to true))
        } catch (e: Exception) {
            call.respondText(e.toString())
        }
    }
}