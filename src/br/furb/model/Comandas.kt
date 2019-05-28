package br.furb.ktorAPI.br.furb.model

import org.jetbrains.exposed.dao.IntIdTable

object Comandas : IntIdTable() {
    val idUsuario = reference("id", Usuarios)
    val produtos = varchar("produtos", 255)
    val valorTotal = decimal("valor_total", 2, 2)
}