package br.com.winestore

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class WinestoreApplication

fun main(args: Array<String>) {
	runApplication<WinestoreApplication>(*args)
}
