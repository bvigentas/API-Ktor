package br.furb.ktorAPI.br.furb.table

import org.jetbrains.exposed.dao.IntIdTable

object Comandas : IntIdTable() {
    val idusuario = reference("id_usuario", Usuarios)
    val produtos = varchar("produtos", 255)
    val valorTotal = decimal("valor_total", 8, 2)
}