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

    fun getComanda(id: Int) : ComandaJson {
        DataBaseConfig.db

        val comanda_ret = transaction {
            SchemaUtils.create(Usuarios, Comandas)
            addLogger(StdOutSqlLogger)
            val comanda =  Comanda.findById(id)
            return@transaction comanda
        }

        return ComandaJson(comanda_ret!!.id.value, comanda_ret.idUsuario.value, comanda_ret.produtos, comanda_ret.valorTotal)
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

    fun updateComanda(id: Int, produtosComanda: String?, valorTotalComanda: BigDecimal?) : ComandaJson{

        DataBaseConfig.db
        val comanda_ret = transaction {
            SchemaUtils.create(Usuarios, Comandas)
            addLogger(StdOutSqlLogger)
            val comanda: Comanda = Comanda.findById(id)!!

            if (produtosComanda == "" || valorTotalComanda == BigDecimal(0)) {
                val comandaNoBanco = Comanda.findById(id)
                if (valorTotalComanda == BigDecimal(0)) comanda.valorTotal = comandaNoBanco!!.valorTotal else comanda.valorTotal = valorTotalComanda!!
                if (produtosComanda == "") comanda.produtos = comandaNoBanco!!.produtos else comanda.produtos = produtosComanda!!
            } else {
                comanda.produtos = produtosComanda!!
                comanda.valorTotal = valorTotalComanda!!
            }

            return@transaction ComandaJson(comanda.id.value, comanda.idUsuario.value, comanda.produtos, comanda.valorTotal)
        }
        return ComandaJson(comanda_ret.id, comanda_ret.idUsuario, comanda_ret.produtos, comanda_ret.valorTotal)
    }

}
