package br.furb.ktorAPI.br.furb.model

import org.jetbrains.exposed.dao.IntIdTable

object Usuarios : IntIdTable(){
    val email = varchar("nome",255)
    val senha = varchar("senha", 255)
}