package br.furb.ktorAPI.br.furb.table

import org.jetbrains.exposed.dao.IntIdTable

object Usuarios : IntIdTable(){
    val email = varchar("email",255)
    val senha = varchar("senha", 255)
}