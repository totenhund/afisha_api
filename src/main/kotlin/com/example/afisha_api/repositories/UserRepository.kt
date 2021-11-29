package com.example.afisha_api.repositories

import com.example.afisha_api.models.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {

    fun findByEmail(email: String): User?

    fun existsByEmail(email: String): Boolean

}