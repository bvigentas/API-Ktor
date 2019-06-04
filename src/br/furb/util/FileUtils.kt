package br.furb.util

import java.io.File
import java.io.FileInputStream
import java.util.*

class FileUtils {

    companion object {
        fun readProperty(property: String) : String {
            val fis = FileInputStream(System.getProperty("user.home")+"\\api-ktor.properties")
            val prop = Properties()
            prop.load(fis)
            return prop.getProperty(property)
        }
    }
}