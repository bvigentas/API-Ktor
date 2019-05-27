package model

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.dao.*

object Usuarios : IntIdTable() {
    val ds_email = varchar("email",50)
    val ds_senha = varchar("senha", 50)
}

class Usuario(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Usuario>(Usuarios)

    var email by Usuarios.ds_email
    var senha by Usuarios.ds_senha
}


fun main(args: Array<String>) {
    Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver")

    transaction {
        addLogger(StdOutSqlLogger)

        SchemaUtils.create (Usuarios)

        Usuario.new {
            email = "ana@furb.br"
            senha = "XyWs0S"
        }

        Usuario.new {
            email = "joao@furb.br"
            senha = "AAzaAcv"
        }

        Usuario.new {
            email = "maria@furb.br"
            senha = "MAgrHns"
        }

        println("Usuários: ${Usuario.all().joinToString {it.email}}")
    }
}