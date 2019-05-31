package br.furb.dao

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction

object DataBaseConfig {

    val db by lazy() {
        Database.connect("jdbc:postgresql://localhost:12346/test",
                     driver = "org.postgresql.Driver",
                     user = "root",
                     password = "root")
    }
}