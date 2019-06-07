package br.furb.dao

import br.furb.model.ComandaJson
import java.math.BigDecimal


interface IDAO<T,A> {
    fun listar(): List<T>

    fun getUnique(id: Int): A

    fun delete(id: Int)

    fun create(model: A): A

    fun update(id: Int, model: A): A

    fun deleteAll()

}