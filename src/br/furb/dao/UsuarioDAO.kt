package br.furb.dao

import br.furb.ktorAPI.br.furb.table.Comandas
import br.furb.ktorAPI.br.furb.table.Usuarios
import br.furb.model.Usuario
import br.furb.model.UsuarioJson
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction

class UsuarioDAO {

    fun createUsuario(emailUsuario: String, senhaUsuario: String): UsuarioJson {
        DataBaseConfig.db

        val usuario = transaction {
            SchemaUtils.create (Usuarios, Comandas)
            addLogger(StdOutSqlLogger)
            return@transaction Usuario.new {
                email = emailUsuario
                senha = senhaUsuario
            }
        }

        return UsuarioJson(usuario.id.value, usuario.email, usuario.senha)
    }

    fun deleteUsuario(id: Int) {
        DataBaseConfig.db

        transaction {
            SchemaUtils.create (Usuarios, Comandas)
            addLogger(StdOutSqlLogger)
            val usuario = Usuario.findById(id)
            usuario?.delete()
        }
    }

    fun getUsuario(id: Int): UsuarioJson {
        DataBaseConfig.db

        val usuario = transaction {
            SchemaUtils.create (Usuarios, Comandas)
            addLogger(StdOutSqlLogger)
            return@transaction Usuario.findById(id)
        }

        return UsuarioJson(usuario!!.id.value, usuario!!.email, usuario.senha)
    }

    fun getUsuarios(): List<Usuario> {
        DataBaseConfig.db

        val usuarios = transaction {
            SchemaUtils.create (Usuarios, Comandas)
            addLogger(StdOutSqlLogger)
            return@transaction Usuario.all().iterator().asSequence().toList()
        }
        return usuarios
    }

    fun deleteUsuarios() {
        DataBaseConfig.db

        val usuarios = getUsuarios()

        for (usuario in usuarios) {
            transaction {
                SchemaUtils.create (Usuarios, Comandas)
                addLogger(StdOutSqlLogger)
                usuario.delete()
            }
        }
    }

    fun updateUsuario(id: Int, emailUsuario: String, senhaUsuario: String): UsuarioJson {

        DataBaseConfig.db

        val usuario = transaction {
            SchemaUtils.create(Usuarios, Comandas)
            addLogger(StdOutSqlLogger)
            val user =  Usuario.findById(id)

            user!!.email = emailUsuario
            user!!.senha = senhaUsuario

            return@transaction UsuarioJson(user.id.value, user.email, user.senha)
        }

        return UsuarioJson(usuario.id, usuario.email, usuario.senha)
    }
}
