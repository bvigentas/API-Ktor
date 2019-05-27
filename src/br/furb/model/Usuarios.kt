package br.furb.ktorAPI.br.furb.model

import org.jetbrains.exposed.sql.Table

object Usuarios : Table(){
    val id = integer("id").primaryKey().autoIncrement()
    val email = varchar("nome",255)
    val senha = varchar("senha", 255)
}