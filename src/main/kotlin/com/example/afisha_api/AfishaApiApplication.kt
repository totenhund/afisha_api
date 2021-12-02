package com.example.afisha_api

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import springfox.documentation.swagger2.annotations.EnableSwagger2

@SpringBootApplication
@EnableSwagger2
class AfishaApiApplication

fun main(args: Array<String>) {
    runApplication<AfishaApiApplication>(*args)
}
