package br.furb.control.comanda

import br.furb.daoImpl.ComandaDAO
import br.furb.model.ComandaJson
import io.ktor.application.call
import io.ktor.auth.OAuth1aException
import io.ktor.features.BadRequestException
import io.ktor.features.NotFoundException
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.*
import io.ktor.util.KtorExperimentalAPI
import org.jetbrains.exposed.exceptions.ExposedSQLException
import java.io.IOException


@KtorExperimentalAPI
val comandaDao = ComandaDAO()


@KtorExperimentalAPI
fun Route.comanda() {
    get("/comandas/{id}") {
        try {
            val comanda = comandaDao.getUnique(call.parameters["id"]!!.toInt())
            call.respond(comanda)
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
    get("/comandas") {
        try {
            val comandas = comandaDao.listar().map { ComandaJson(it) }
            call.respond(comandas)
        } catch (e: OAuth1aException) {
            call.respond(HttpStatusCode.Unauthorized)
        } catch (e: BadRequestException){
            call.respond(HttpStatusCode.BadRequest)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.InternalServerError)
        }
    }
    post("/comandas") {
        try {
            var comanda = call.receive<ComandaJson>()
            comanda = comandaDao.create(comanda)
            call.respond(comanda)
        } catch(e: IOException){
            call.respond("" + e.message)
        } catch (e: ExposedSQLException){
            call.respond("Impossivel criar comanda, id do usuário é inexistente")
        } catch (e: OAuth1aException) {
            call.respond(HttpStatusCode.Unauthorized)
        } catch (e: BadRequestException){
            call.respond(HttpStatusCode.BadRequest)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.InternalServerError)
        }
    }
    put("/comandas/{id}") {
        try {
            var comanda = call.receive<ComandaJson>()
            comanda = comandaDao.update(
                call.parameters["id"]!!.toInt(),
                comanda
            )

            // mudar a saida aqui - colocar string e to json

            call.respond(comanda)

        } catch (e: NotFoundException) {
            call.respond(HttpStatusCode.NotFound)
        } catch (e: IOException) {
            call.respond("" + e.message)
        } catch (e: OAuth1aException) {
            call.respond(HttpStatusCode.Unauthorized)
        } catch (e: BadRequestException) {
            call.respond(HttpStatusCode.BadRequest)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.InternalServerError)
        }
    }
    delete("/comandas/{id}") {
        try {
            comandaDao.delete(call.parameters["id"]!!.toInt())
            call.respond(mapOf("success" to mapOf("text" to "comanda removida")))
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
}