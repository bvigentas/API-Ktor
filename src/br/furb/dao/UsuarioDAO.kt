package br.furb.dao

import br.furb.ktorAPI.br.furb.table.Comandas
import br.furb.ktorAPI.br.furb.table.Usuarios
import br.furb.model.Usuario
import br.furb.model.UsuarioJson
import io.ktor.features.NotFoundException
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.IOException

class UsuarioDAO {

    fun authenticateUsuario(emailUsuario: String, senhaUsuario: String){
        if(emailUsuario.equals("") ||  !emailUsuario.contains("@"))
        {
            throw IOException("Email inválido")
        }
        else if(senhaUsuario.length <= 5 ) {
            throw IOException("Senha inválida")
        }
    }

    fun createUsuario(emailUsuario: String, senhaUsuario: String): UsuarioJson {

        authenticateUsuario(emailUsuario, senhaUsuario)

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

    fun getUsuario(id: Int): UsuarioJson {
        DataBaseConfig.db
        try {
            val usuario = transaction {
                SchemaUtils.create(Usuarios, Comandas)
                addLogger(StdOutSqlLogger)
                return@transaction Usuario.findById(id)
            }

            return UsuarioJson(usuario!!.id.value, usuario!!.email, usuario.senha)
        } catch (e: Exception){
            throw NotFoundException()
        }
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

    fun updateUsuario(id: Int, emailUsuario: String?, senhaUsuario: String?): UsuarioJson {

        if(emailUsuario != null && senhaUsuario != null)
            authenticateUsuario(emailUsuario, senhaUsuario)

        DataBaseConfig.db

        try {
            val usuario = transaction {

                SchemaUtils.create(Usuarios, Comandas)
                addLogger(StdOutSqlLogger)
                val user: Usuario = Usuario.findById(id)!!

                if (senhaUsuario == "" || emailUsuario == "") {
                    val usuarioNoBanco = Usuario.findById(id)
                    if (emailUsuario == "") user.email = usuarioNoBanco!!.email else user.email = emailUsuario!!
                    if (senhaUsuario == "") user.senha = usuarioNoBanco!!.senha else user.senha = senhaUsuario!!
                } else {
                    user.email = emailUsuario!!
                    user.senha = senhaUsuario!!
                }

                return@transaction UsuarioJson(user!!.id.value, user.email, user.senha)
            }

            return UsuarioJson(usuario.id, usuario.email, usuario.senha)
        } catch (e: Exception){
            throw NotFoundException()
        }
    }
}
