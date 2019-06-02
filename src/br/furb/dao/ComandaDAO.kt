package br.furb.dao

import br.furb.ktorAPI.br.furb.table.Comandas
import br.furb.ktorAPI.br.furb.table.Usuarios
import br.furb.model.Comanda
import br.furb.model.ComandaHolder
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

        val comandas = transaction {
            SchemaUtils.create (Usuarios, Comandas)
            addLogger(StdOutSqlLogger)
            return@transaction Comanda.all()
        }
        return comandas
    }

    fun getComanda(id: Int): Comanda? {
        DataBaseConfig.db

        transaction {
            SchemaUtils.create(Usuarios, Comandas)
            addLogger(StdOutSqlLogger)
            val comanda =  Comanda.findById(id)
            return@transaction comanda
        }
        return null
    }

    fun deleteComanda(id: Int) {
        DataBaseConfig.db

        transaction {
            SchemaUtils.create (Usuarios, Comandas)
            addLogger(StdOutSqlLogger)
            val usuario = Comanda.findById(id)
            usuario?.delete()
        }
    }

    fun createComanda(comanda: Comanda) {
        DataBaseConfig.db
        transaction {
            SchemaUtils.create (Usuarios, Comandas)
            addLogger(StdOutSqlLogger)
            Comanda.new {
                idUsuario = comanda.idUsuario
                produtos = comanda.produtos
                valorTotal = comanda.valorTotal
            }
        }
    }
}