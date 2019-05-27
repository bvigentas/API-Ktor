package model

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.dao.*

object Comandas : IntIdTable() {

    //val cd_usuario = reference("cd_usuario", Usuarios)
    val ds_produto = varchar("ds_produto", 50)
    val vl_total =  float("vl_total")
}

class Comanda(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Comanda>(Comandas)

    //var usuario by Usuario referencedOn Usuarios.id
    var produtos by Comandas.ds_produto
    var valorTotal by Comandas.vl_total
}


fun main(args: Array<String>) {
    Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver")

    transaction {
        addLogger(StdOutSqlLogger)

        SchemaUtils.create (Comandas, Usuarios)

        Comanda.new {
            //usuario = 1
            produtos = "X-Salad$20#X-Bacon$30"
            valorTotal = 50.0F
        }

        Comanda.new {
            //usuario = 2
            produtos = "X-File$35#X-Bacon$30"
            valorTotal = 55.0F
        }

        Comanda.new {
            //usuario = 3
            produtos = "X-Calabresa$19#X-X-Salad$20"
            valorTotal = 39.0F
        }

        println("Comandas: ${Comanda.all()}")
    }
}