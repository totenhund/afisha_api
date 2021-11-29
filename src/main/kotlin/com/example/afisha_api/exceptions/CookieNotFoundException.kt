package com.example.afisha_api.exceptions

import org.springframework.security.core.AuthenticationException

class CookieNotFoundException(msg: String) : AuthenticationException(msg)