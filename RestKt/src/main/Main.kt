package main

import io.ktor

fun Application.module() {
    install(DefaultHeaders)
    install(CallLogging)
    install(ContentNegotiation) {
        jackson {
            configure(SerializationFeature.INDENT_OUTPUT, true)
        }
    }

    install(Routing) {
        widget(WidgetService())
    }
}

fun main(args: Array<String>) {
    println("The application has started")
    embeddedServer(Netty, 8080, module = Application::module).start()
}
