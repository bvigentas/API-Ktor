package br.furb.daoImpl

import br.furb.config.DataBaseConfig
import br.furb.dao.IDAO
import br.furb.ktorAPI.br.furb.table.Comandas
import br.furb.ktorAPI.br.furb.table.Usuarios
import br.furb.model.Usuario
import br.furb.model.UsuarioJson
import io.ktor.features.NotFoundException
import io.ktor.util.KtorExperimentalAPI
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction

@KtorExperimentalAPI
class UsuarioDAO: IDAO<Usuario, UsuarioJson> {
    override fun listar(): List<Usuario> {
        DataBaseConfig.db

        val usuarios = transaction {
            SchemaUtils.create (Usuarios, Comandas)
            addLogger(StdOutSqlLogger)
            return@transaction Usuario.all().iterator().asSequence().toList()
        }
        return usuarios
    }

    override fun getUnique(id: Int): UsuarioJson {
        DataBaseConfig.db

        val usuario = transaction {
            SchemaUtils.create (Usuarios, Comandas)
            addLogger(StdOutSqlLogger)
            return@transaction Usuario.findById(id)
        }

        return UsuarioJson(usuario!!.id.value, usuario.email, usuario.senha)
    }

    override fun delete(id: Int) {
        DataBaseConfig.db

        try {
            transaction {
                SchemaUtils.create(Usuarios, Comandas)
                addLogger(StdOutSqlLogger)
                val usuario = Usuario.findById(id)
                usuario?.delete()
            }
        } catch (e: Exception){
            throw NotFoundException()
        }
    }

    override fun create(model: UsuarioJson): UsuarioJson {
        DataBaseConfig.db

        val usuario = transaction {
            SchemaUtils.create (Usuarios, Comandas)
            addLogger(StdOutSqlLogger)
            return@transaction Usuario.new {
                email = model.email
                senha = model.senha
            }
        }

        return UsuarioJson(usuario.id.value, usuario.email, usuario.senha)
    }

    override fun update(id: Int, model: UsuarioJson): UsuarioJson {
        DataBaseConfig.db

        try {
            val usuario = transaction {

                SchemaUtils.create(Usuarios, Comandas)
                addLogger(StdOutSqlLogger)
                val user: Usuario = Usuario.findById(id)!!

                if (model.senha == "" || model.email == "") {
                    val usuarioNoBanco = Usuario.findById(id)
                    if (model.email == "") user.email = usuarioNoBanco!!.email else user.email = model.email
                    if (model.senha == "") user.senha = usuarioNoBanco!!.senha else user.senha = model.senha
                } else {
                    user.email = model.email
                    user.senha = model.senha
                }

                return@transaction UsuarioJson(user.id.value, user.email, user.senha)
            }

            return UsuarioJson(usuario.id, usuario.email, usuario.senha)
        } catch (e: Exception){
            throw NotFoundException()
        }
    }

    override fun deleteAll() {
        DataBaseConfig.db

        val usuarios = listar()

        for (usuario in usuarios) {
            transaction {
                SchemaUtils.create (Usuarios, Comandas)
                addLogger(StdOutSqlLogger)
                usuario.delete()
            }
        }
    }
}
