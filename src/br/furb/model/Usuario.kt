package br.furb.model

import br.furb.ktorAPI.br.furb.table.Usuarios
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass

class Usuario(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Usuario>(Usuarios)

    var email by Usuarios.email
    var senha by Usuarios.senha
}