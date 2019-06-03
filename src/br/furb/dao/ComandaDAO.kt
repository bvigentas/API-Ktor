package br.furb.dao

import br.furb.ktorAPI.br.furb.table.Comandas
import br.furb.ktorAPI.br.furb.table.Usuarios
import br.furb.model.Comanda
import br.furb.model.ComandaJson
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction
import java.math.BigDecimal

class ComandaDAO {

    fun getComandas(): List<Comanda> {
        DataBaseConfig.db

        val comandas = transaction {
            SchemaUtils.create (Usuarios, Comandas)
            addLogger(StdOutSqlLogger)
            return@transaction Comanda.all().iterator().asSequence().toList()
        }
        return comandas
    }

    fun getComanda(id: Int) {
        DataBaseConfig.db

        transaction {
            SchemaUtils.create(Usuarios, Comandas)
            addLogger(StdOutSqlLogger)
            val comanda =  Comanda.findById(id)
            return@transaction comanda
        }
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

    fun createComanda(idUsuarioComanda: Int, produtosComanda: String, valorTotalComanda: BigDecimal): ComandaJson {
        DataBaseConfig.db
        val comanda = transaction {
            SchemaUtils.create (Usuarios, Comandas)
            addLogger(StdOutSqlLogger)
            return@transaction Comanda.new {
                idUsuario = EntityID(idUsuarioComanda, Usuarios)
                produtos = produtosComanda
                valorTotal = valorTotalComanda
            }
        }

        return ComandaJson(comanda.id.value, comanda.idUsuario.value, comanda.produtos, comanda.valorTotal)
    }
}