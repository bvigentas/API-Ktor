package br.furb.model

import br.furb.ktorAPI.br.furb.table.Comandas
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import java.math.BigDecimal

class Comanda(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Comanda>(Comandas)

    var idUsuario by Comandas.idUsuario
    var produtos by Comandas.produtos
    var valorTotal by Comandas.valorTotal
}

class ComandaJson(val id: Int = 0, val idUsuario: Int = 0, val produtos: String = "", val valorTotal: BigDecimal = BigDecimal(0)){
    constructor(comanda: Comanda): this(comanda.id.value, comanda.idUsuario.value, comanda.produtos, comanda.valorTotal)
}
