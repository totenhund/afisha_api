package com.example.afisha_api.utils

import com.example.afisha_api.models.User
import com.example.afisha_api.security.SecurityUser
import org.springframework.security.authentication.AnonymousAuthenticationToken
import org.springframework.security.core.Authentication

class SecurityUtil {
    companion object {
        fun getUser(authentication: Authentication): User {
            if (authentication is AnonymousAuthenticationToken) {
                throw RuntimeException("Anonymous authentication")
            }
            return (authentication.principal as SecurityUser).entity
        }
    }
}