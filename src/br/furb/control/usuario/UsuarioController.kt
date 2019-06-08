package br.furb.control.usuario

import br.furb.daoImpl.UsuarioDAO
import br.furb.model.UsuarioJson
import io.ktor.application.call
import io.ktor.auth.OAuth1aException
import io.ktor.features.BadRequestException
import io.ktor.features.NotFoundException
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.*
import io.ktor.util.KtorExperimentalAPI
import org.jetbrains.exposed.exceptions.ExposedSQLException
import java.io.IOException


@KtorExperimentalAPI
val usuarioDao = UsuarioDAO()

@KtorExperimentalAPI
fun Route.usuario() {
    get("/usuarios") {
        try {
            val usuarios = usuarioDao.listar().map { UsuarioJson(it) }
            call.respond(usuarios)
        } catch (e: OAuth1aException) {
            call.respond(HttpStatusCode.Unauthorized)
        } catch (e: BadRequestException){
            call.respond(HttpStatusCode.BadRequest)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.InternalServerError)
        }
    }
    get("/usuarios/{id}") {

        try {
            // val usuario = usuarioDao.getUsuarios().map { UsuarioJson(it) }.get(call.parameters["id"]!!.toInt() - 1)
            val usuario = usuarioDao.getUnique(call.parameters["id"]!!.toInt())
            call.respond(usuario)
        } catch (e: NotFoundException) {
            call.respond(HttpStatusCode.NotFound)
        } catch (e: OAuth1aException) {
            call.respond(HttpStatusCode.Unauthorized)
        } catch (e: BadRequestException){
            call.respond(HttpStatusCode.BadRequest)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.InternalServerError)
        }
    }
    post("/usuarios") {
        try {
            var usuario = call.receive<UsuarioJson>()
            usuario = usuarioDao.create(usuario)
            call.respond(usuario)
        } catch(e: IOException){
            call.respond("" + e.message)
        } catch (e: OAuth1aException) {
            call.respond(HttpStatusCode.Unauthorized)
        } catch (e: BadRequestException){
            call.respond(HttpStatusCode.BadRequest)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.InternalServerError)
        }
    }
    put("/usuarios/{id}") {
        try {
            var usuario = call.receive<UsuarioJson>()

            if(usuario.id != call.parameters["id"]!!.toInt() && usuario.id != 0){
                throw (IOException("Impossível alterar o id do usuário"))
            }

            usuario = usuarioDao.update(
                call.parameters["id"]!!.toInt(),
                usuario
            )

            call.respond(usuario)

        } catch (e: NotFoundException) {
            call.respond(HttpStatusCode.NotFound)
        } catch(e: IOException){
            call.respond("" + e.message)
        } catch (e: OAuth1aException) {
            call.respond(HttpStatusCode.Unauthorized)
        } catch (e: BadRequestException){
            call.respond(HttpStatusCode.BadRequest)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.InternalServerError)
        }
    }
    delete("/usuarios/{id}") {
        try {
            usuarioDao.delete(call.parameters["id"]!!.toInt())
            call.respond(mapOf(true to true))
        } catch (e: NotFoundException) {
            call.respond(HttpStatusCode.NotFound)
        } catch (e: ExposedSQLException) {
            call.respondText("Não foi possível deletar o usuário. Há uma comanda vinculada a ele.")
        } catch (e: Exception) {
            call.respondText(e.toString())
        }
    }
    delete("/usuarios") {
        try {
            usuarioDao.deleteAll()
            call.respond(mapOf(true to true))
        } catch (e: OAuth1aException) {
            call.respond(HttpStatusCode.Unauthorized)
        } catch (e: BadRequestException){
            call.respond(HttpStatusCode.BadRequest)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.InternalServerError)
        }
    }
}