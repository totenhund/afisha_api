package com.example.afisha_api.security

import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class AuthenticationFailureHandlerImpl : AuthenticationFailureHandler {
    override fun onAuthenticationFailure(
            request: HttpServletRequest,
            response: HttpServletResponse,
            e: AuthenticationException
    ) {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.message)
    }
}