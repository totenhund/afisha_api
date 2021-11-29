package com.example.afisha_api.security

import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletResponse

class RememberMeServiceImpl(key: String, userDetailsService: UserDetailsService) :
        TokenBasedRememberMeServices(key, userDetailsService) {

    fun dropCookies(response: HttpServletResponse) {
        val cookie = Cookie(cookieName, null)
        cookie.maxAge = 0
        cookie.path = "/"
        cookie.isHttpOnly = true;
        response.addCookie(cookie)
    }
}