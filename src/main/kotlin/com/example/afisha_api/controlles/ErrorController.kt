package com.example.afisha_api.controlles

import com.example.afisha_api.exceptions.ForbiddenException
import com.example.afisha_api.exceptions.ObjectNotFoundException
import com.example.afisha_api.exceptions.ServerErrorException
import io.swagger.annotations.ApiOperation
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import org.springframework.web.bind.ServletRequestBindingException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import java.io.IOException
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.validation.ValidationException


@ControllerAdvice
@ApiOperation(value = "This controller is used to process errors", hidden = true)
class ErrorController(
        private val authFailureHandler: AuthenticationFailureHandler,
) {
    @ExceptionHandler(
            IllegalArgumentException::class, ServletRequestBindingException::class,
            HttpMessageNotReadableException::class, MethodArgumentTypeMismatchException::class
    )
    @Throws(IOException::class)
    fun notValid(response: HttpServletResponse, e: Exception) {
        response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.message)
    }

    @ExceptionHandler(
            ServerErrorException::class
    )
    @Throws(IOException::class)
    fun serverError(response: HttpServletResponse, e: Exception) {
        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.message)
    }

    @ExceptionHandler(
            ObjectNotFoundException::class
    )
    @Throws(IOException::class)
    fun dontExist(response: HttpServletResponse, e: Exception) {
        response.sendError(HttpServletResponse.SC_NOT_FOUND, e.message)
    }

    @ExceptionHandler(
            ValidationException::class
    )
    @Throws(IOException::class)
    fun validationError(response: HttpServletResponse, e: Exception) {
        response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.message)
    }

    @ExceptionHandler(
            ForbiddenException::class
    )
    @Throws(IOException::class)
    fun forbiddenError(response: HttpServletResponse, e: Exception) {
        response.sendError(HttpServletResponse.SC_FORBIDDEN, e.message)
    }

    @ExceptionHandler(AuthenticationException::class)
    @Throws(
            IOException::class,
            ServletException::class
    )
    fun authFailed(request: HttpServletRequest?, response: HttpServletResponse?, e: java.lang.Exception?) {
        authFailureHandler.onAuthenticationFailure(request, response, e as AuthenticationException?)
    }
}