package br.furb.util

import java.io.FileInputStream
import java.nio.file.Paths
import java.util.*

class FileUtils {

    companion object {
        fun readProperty(property: String) : String {
            val path = Paths.get(System.getProperty("user.home")).resolve("api-ktor.properties")
            val fis = FileInputStream(path.toString())
            val prop = Properties()
            prop.load(fis)
            return prop.getProperty(property)
        }
    }
}