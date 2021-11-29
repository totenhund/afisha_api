package com.example.afisha_api.security

import com.example.afisha_api.models.User as UserEntity
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.User

class SecurityUser(
        @Transient
        @javax.persistence.Transient
        val entity: UserEntity,
        authorities: List<GrantedAuthority>,
) : User(entity.email, entity.password, authorities) {
}