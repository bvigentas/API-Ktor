package br.furb.ktorAPI.br.furb.model

import org.jetbrains.exposed.dao.IntIdTable

object Comandas : IntIdTable() {
    val idUsuario = (integer("id_usuario") references Usuarios.id)
    val produtos = varchar("produtos", 255)
    val valorTotal = decimal("valor_total", 2, 2)
}