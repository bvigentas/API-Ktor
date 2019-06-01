package br.furb.dao

import br.furb.ktorAPI.br.furb.table.Comandas
import br.furb.ktorAPI.br.furb.table.Usuarios
import br.furb.model.Comanda
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.SizedIterable
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction
import java.math.BigDecimal

class ComandaDAO {

    fun getComandas(): SizedIterable<Comanda> {
        DataBaseConfig.db
        SchemaUtils.create (Usuarios, Comandas)

        val comandas = transaction {
            addLogger(StdOutSqlLogger)
            return@transaction Comanda.all()
        }
        return comandas
    }

    fun getComanda(id: Int): Comanda? {
        DataBaseConfig.db
        SchemaUtils.create(Usuarios, Comandas)

        val comanda = transaction {
            addLogger(StdOutSqlLogger)
            return@transaction Comanda.findById(id)
        }
        return comanda
    }

    fun deleteComanda(id: Int) {
        DataBaseConfig.db
        SchemaUtils.create (Usuarios, Comandas)

        transaction {
            addLogger(StdOutSqlLogger)
            val usuario = Comanda.findById(id)
            usuario?.delete()
        }
    }

    fun createComanda(idUsuarioComanda: EntityID<Int>, produtosComanda: String, valorComanda: BigDecimal) {
        DataBaseConfig.db
        SchemaUtils.create (Usuarios, Comandas)

        transaction {
            addLogger(StdOutSqlLogger)
            Comanda.new {
                idUsuario = idUsuarioComanda
                produtos = produtosComanda
                valorTotal = valorComanda
            }
        }
    }
}