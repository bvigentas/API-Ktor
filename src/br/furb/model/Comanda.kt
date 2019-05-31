package br.furb.model

import br.furb.ktorAPI.br.furb.table.Comandas
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass

class Comanda(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Comanda>(Comandas)

    var idUsuario by Comandas.idUsuario
    var produtos by Comandas.produtos
    var valorTotal by Comandas.valorTotal
}