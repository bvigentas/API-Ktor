package br.furb.config

import org.jetbrains.exposed.sql.Database

object DataBaseConfig {

    val db by lazy() {
        Database.connect("jdbc:postgresql://localhost:5432/postgres",
                     driver = "org.postgresql.Driver",
                     user = "postgres",
                     password = "root")
    }
}