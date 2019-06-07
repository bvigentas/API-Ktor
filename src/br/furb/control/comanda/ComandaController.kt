package br.furb.control.comanda

import br.furb.daoImpl.ComandaDAO
import br.furb.model.ComandaJson
import io.ktor.application.call
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.*


val comandaDao = ComandaDAO()

fun Route.comanda() {
    get("/comandas/{id}") {
        try {
            val comanda = comandaDao.getUnique(call.parameters["id"]!!.toInt())
            call.respond(comanda)
        } catch (e: Exception) {

        }
    }
    get("/comandas") {
        try {
            val comandas = comandaDao.listar().map { ComandaJson(it) }
            call.respond(comandas)
        } catch (e: Exception) {
            call.respond(e)
        }
    }
    post("/comandas") {
        try {
            var comanda = call.receive<ComandaJson>()
            comanda = comandaDao.create(comanda)
            call.respond(comanda)
        } catch (e: Exception) {
//                    call.respond(e)
            e.printStackTrace()
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

        } catch (e: Exception) {
            call.respondText(e.toString())
        }
    }
    delete("/comandas/{id}") {
        try {
            comandaDao.delete(call.parameters["id"]!!.toInt())
            call.respond(mapOf("success" to mapOf("text" to "comanda removida")))
        } catch (e: Exception) {
            call.respondText(e.toString())
        }
    }
}