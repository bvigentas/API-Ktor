package br.furb.daoImpl

import br.furb.config.DataBaseConfig
import br.furb.dao.IDAO
import br.furb.ktorAPI.br.furb.table.Comandas
import br.furb.ktorAPI.br.furb.table.Usuarios
import br.furb.model.Comanda
import br.furb.model.ComandaJson
import io.ktor.features.NotFoundException
import io.ktor.util.KtorExperimentalAPI
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction
import java.math.BigDecimal

@KtorExperimentalAPI
class ComandaDAO: IDAO<Comanda, ComandaJson> {
    override fun listar(): List<Comanda> {
        DataBaseConfig.db

        val comandas = transaction {
            SchemaUtils.create (Usuarios, Comandas)
            addLogger(StdOutSqlLogger)
            return@transaction Comanda.all().iterator().asSequence().toList()
        }
        return comandas
    }

    override fun getUnique(id: Int): ComandaJson {
        DataBaseConfig.db
        try {
            val comanda_ret = transaction {
                SchemaUtils.create(Usuarios, Comandas)
                addLogger(StdOutSqlLogger)
                val comanda = Comanda.findById(id)
                return@transaction comanda
            }

            return ComandaJson(
                comanda_ret!!.id.value,
                comanda_ret.idusuario.value,
                comanda_ret.produtos,
                comanda_ret.valorTotal
            )
        } catch (e: Exception){
            throw NotFoundException()
        }
    }

    override fun delete(id: Int) {
        DataBaseConfig.db
        try {
            transaction {
                SchemaUtils.create(Usuarios, Comandas)
                addLogger(StdOutSqlLogger)
                val usuario = Comanda.findById(id)
                usuario?.delete()
            }
        } catch (e: Exception){
            throw NotFoundException()
        }
    }


    override fun create(model: ComandaJson): ComandaJson {
        DataBaseConfig.db

        val comanda = transaction {
            SchemaUtils.create (Usuarios, Comandas)
            addLogger(StdOutSqlLogger)
            return@transaction Comanda.new {

                idusuario = EntityID(model.id, Usuarios)
                produtos = model.produtos
                valorTotal = model.valorTotal
            }
        }

        return ComandaJson(comanda.id.value, comanda.idusuario.value, comanda.produtos, comanda.valorTotal)
    }

    override fun update(id: Int, model: ComandaJson): ComandaJson {
        DataBaseConfig.db
        try {
            val comanda_ret = transaction {
                SchemaUtils.create(Usuarios, Comandas)
                addLogger(StdOutSqlLogger)
                val comanda: Comanda = Comanda.findById(id)!!

                if (model.produtos == "" || model.valorTotal == BigDecimal(0)) {
                    val comandaNoBanco = Comanda.findById(id)
                    if (model.valorTotal == BigDecimal(0)) comanda.valorTotal =
                        comandaNoBanco!!.valorTotal else comanda.valorTotal = model.valorTotal
                    if (model.produtos == "") comanda.produtos = comandaNoBanco!!.produtos else comanda.produtos =
                        model.produtos
                } else {
                    comanda.produtos = model.produtos
                    comanda.valorTotal = model.valorTotal
                }

                return@transaction ComandaJson(comanda.id.value, comanda.idusuario.value, comanda.produtos, comanda.valorTotal)

            }

            return ComandaJson(comanda_ret.id, comanda_ret.idusuario, comanda_ret.produtos, comanda_ret.valorTotal)
        } catch (e: Exception){
            throw NotFoundException()
        }
    }

    override fun deleteAll() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
