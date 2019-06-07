package br.furb.ktorAPI.br.furb.model

import br.furb.control.AuthentificationOAuth
import br.furb.dao.ComandaDAO
import br.furb.dao.UsuarioDAO
import br.furb.model.*
import br.furb.util.FileUtils
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.client.*
import io.ktor.client.engine.apache.*
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.request.*
import io.ktor.routing.*
import io.ktor.gson.*
import io.ktor.features.*
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.sessions.*
import io.ktor.util.KtorExperimentalAPI
import io.ktor.util.hex
import io.ktor.websocket.WebSockets
import org.apache.http.HttpStatus
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.postgresql.util.PSQLException
import java.io.IOException

class MySession(val userId: String)

@KtorExperimentalAPI
fun main (args: Array<String>) {
    embeddedServer(Netty, port = 8083) {

        install(WebSockets)

        install(Sessions) {
            cookie<MySession>("oauthSampleSessionId") {
                val secretSignKey = hex(FileUtils.readProperty("oauth.secretSignKey"))
                transform(SessionTransportTransformerMessageAuthentication(secretSignKey))
            }
        }
        install(Authentication) {
            oauth("google-oauth") {
                client = HttpClient{Apache}
                providerLookup = { AuthentificationOAuth.googleOauthProvider }
                urlProvider = {
                    redirectUrl("/login")
                }
            }
        }

       install(ContentNegotiation) {
           gson {
               setPrettyPrinting()
           }
       }

        val usuarioDao = UsuarioDAO()
        val comandaDao = ComandaDAO()

        routing {
            route("/RestAPIFurb") {
                get("/usuarios") {
                    try {
                        val usuarios = usuarioDao.getUsuarios().map { UsuarioJson(it) }
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
                        val usuario = usuarioDao.getUsuario(call.parameters["id"]!!.toInt())
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
                        usuario = usuarioDao.createUsuario(usuario.email, usuario.senha)
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
                        var parameters = call.receive<UsuarioJson>()

                        if(parameters.id != call.parameters["id"]!!.toInt() && parameters.id != 0){
                            throw (IOException("Impossível alterar o id do usuário"))
                        }

                        val usuario = usuarioDao.updateUsuario(
                            call.parameters["id"]!!.toInt(),
                            parameters.email,
                            parameters.senha
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
                        usuarioDao.deleteUsuario(call.parameters["id"]!!.toInt())
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
                        usuarioDao.deleteUsuarios()
                        call.respond(mapOf(true to true))
                    }
                    catch (e: OAuth1aException) {
                        call.respond(HttpStatusCode.Unauthorized)
                    } catch (e: BadRequestException){
                        call.respond(HttpStatusCode.BadRequest)
                    } catch (e: Exception) {
                        call.respond(HttpStatusCode.InternalServerError)
                    }
                }



                get("/comandas/{id}") {
                    try {
                        val comanda = comandaDao.getComanda(call.parameters["id"]!!.toInt())
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
                        val comandas = comandaDao.getComandas().map { ComandaJson(it) }
                        call.respond(comandas)
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
                        var parameters = call.receive<ComandaJson>()
                        val comanda = comandaDao.updateComanda(
                            call.parameters["id"]!!.toInt(),
                            parameters.produtos,
                            parameters.valorTotal
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

                post("/comandas") {
                    try {
                        var comanda = call.receive<ComandaJson>()
                        comanda = comandaDao.createComanda(comanda.idusuario, comanda.produtos, comanda.valorTotal)
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

                delete("/comandas/{id}") {
                    try {
                        comandaDao.deleteComanda(call.parameters["id"]!!.toInt())
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

            authenticate("google-oauth") {
                route("/login") {
                    handle {
                        val principal = call.authentication.principal<OAuthAccessTokenResponse.OAuth2>()
                            ?: error("No principal")

                        val json = HttpClient{Apache}.get<String>("https://www.googleapis.com/userinfo/v2/me") {
                            header("Authorization", "Bearer ${principal.accessToken}")
                        }
                        val typeReference = object : TypeReference<Any?>(){}
                        val data = ObjectMapper().readValue<Map<String, Any?>>(json, typeReference)
                        val id = data["id"] as String?

                        if (id != null) {
                            call.sessions.set(MySession(id))
                        }
                        call.respondRedirect("/")
                    }
                }
            }

        }
    }.start(wait = true)
}

private fun ApplicationCall.redirectUrl(path: String): String {
    val defaultPort = if (request.origin.scheme == "http") 80 else 443
    val hostPort = request.host() + request.port().let { port -> if (port == defaultPort) "" else ":$port" }
    val protocol = request.origin.scheme
    return "$protocol://$hostPort$path"
}
