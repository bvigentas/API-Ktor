package br.furb.control

import br.furb.util.FileUtils
import io.ktor.auth.OAuthServerSettings
import io.ktor.http.HttpMethod

object AuthentificationOAuth{
    val googleOauthProvider = OAuthServerSettings.OAuth2ServerSettings(
        name = "google",
        authorizeUrl = "https://accounts.google.com/o/oauth2/auth",
        accessTokenUrl = "https://www.googleapis.com/oauth2/v3/token",
        requestMethod = HttpMethod.Post,

        clientId = FileUtils.readProperty("google.clientId"),
        clientSecret = FileUtils.readProperty("google.clientSecret"),
        defaultScopes = listOf("profile") // no email, but gives full name, picture, and id
    )
}