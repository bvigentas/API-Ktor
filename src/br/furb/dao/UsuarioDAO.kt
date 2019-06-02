package br.furb.dao

import br.furb.ktorAPI.br.furb.table.Comandas
import br.furb.ktorAPI.br.furb.table.Usuarios
import br.furb.model.Usuario
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.SizedIterable
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction

class UsuarioDAO {

    fun createUsuario(emailUsuario: String, senhaUsuario: String) {
        DataBaseConfig.db

        transaction {
            SchemaUtils.create (Usuarios, Comandas)
            addLogger(StdOutSqlLogger)
            Usuario.new {
                email = emailUsuario
                senha = senhaUsuario
            }
        }
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

    fun getUsuario(id: Int): Usuario? {
        DataBaseConfig.db

        val usuario = transaction {
            SchemaUtils.create (Usuarios, Comandas)
            addLogger(StdOutSqlLogger)
            val usuario = Usuario.findById(id)
            return@transaction usuario
        }

        return usuario
    }

    fun getUsuarios(): SizedIterable<Usuario> {
        DataBaseConfig.db

        val usuarios = transaction {
            SchemaUtils.create (Usuarios, Comandas)
            addLogger(StdOutSqlLogger)
            return@transaction Usuario.all()
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
}