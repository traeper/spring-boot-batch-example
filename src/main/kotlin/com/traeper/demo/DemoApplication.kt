package com.traeper.demo

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration
import org.springframework.boot.runApplication
import kotlin.system.exitProcess

@SpringBootApplication(exclude = [DataSourceTransactionManagerAutoConfiguration::class])
class DemoApplication

fun main(args: Array<String>) {
	val applicationContext = runApplication<DemoApplication>(*args)
	exitProcess(SpringApplication.exit(applicationContext))
}

