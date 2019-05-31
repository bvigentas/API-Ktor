package br.furb.dao

import br.furb.model.Comanda
import org.jetbrains.exposed.sql.SizedIterable
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction

class ComandaDAO {

    fun getComandas(): SizedIterable<Comanda> {
        DataBaseConfig.db

        val comandas = transaction {
            addLogger(StdOutSqlLogger)
            return@transaction Comanda.all()
        }
        return comandas
    }
}