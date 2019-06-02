package br.furb.dao

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction

object DataBaseConfig {

    val db by lazy() {
        Database.connect("jdbc:postgresql://localhost:5432/postgres",
                     driver = "org.postgresql.Driver",
                     user = "postgres",
                     password = "root")
    }
}