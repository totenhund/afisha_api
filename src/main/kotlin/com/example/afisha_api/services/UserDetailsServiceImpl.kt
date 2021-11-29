package com.example.afisha_api.services

import com.example.afisha_api.repositories.UserRepository
import com.example.afisha_api.security.SecurityUser
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class UserDetailsServiceImp : UserDetailsService {
    @Autowired
    lateinit var userRepository: UserRepository

    override fun loadUserByUsername(username: String): UserDetails {
        val userEntity = userRepository.findByEmail(username) ?: throw UsernameNotFoundException("User not found")

        val authorities: MutableList<GrantedAuthority> = ArrayList()
        authorities.add(SimpleGrantedAuthority(userEntity.role.name))

        return SecurityUser(userEntity, authorities)
    }
}