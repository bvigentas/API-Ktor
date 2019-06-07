package br.furb.ktorAPI.br.furb.model

import br.furb.security.AuthentificationOAuth
import br.furb.daoImpl.ComandaDAO
import br.furb.daoImpl.UsuarioDAO
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
import br.furb.control.comanda.comanda
import br.furb.control.usuario.usuario

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

        routing {
            route("/RestAPIFurb") {
                comanda()
                usuario()
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