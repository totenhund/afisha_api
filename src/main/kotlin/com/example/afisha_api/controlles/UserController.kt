package com.example.afisha_api.controlles

import com.example.afisha_api.exceptions.CookieNotFoundException
import com.example.afisha_api.helpers.*
import com.example.afisha_api.security.RememberMeServiceImpl
import com.example.afisha_api.services.UserService
import com.example.afisha_api.utils.SecurityUtil.Companion.getUser
import com.example.afisha_api.utils.Validator
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.RememberMeServices
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("/users")
class UserController(
    private val userService: UserService,
    private val authProvider: AuthenticationProvider,
    private val validator: Validator,
    private val rememberMeServices: RememberMeServices,
) {

    @Autowired
    private lateinit var request: HttpServletRequest

    @Autowired
    private lateinit var response: HttpServletResponse

    @PostMapping
    @RequestMapping("/register")
    fun register(@RequestBody user: UserVO): ResponseEntity<Result> {
        validator.validateEmail(user.email)
        userService.createUser(user)
        return ok(Result("You are registered!"))
    }

    @PostMapping
    @RequestMapping("/login")
    fun login(@RequestBody userLoginVO: UserLoginVO): ResponseEntity<UserVO> {
        validator.validateEmail(userLoginVO.email)
        try {
            rememberMeAuthenticate(userLoginVO.email, userLoginVO.password)
        }catch (e: AuthenticationException){
            authenticate(userLoginVO.email, userLoginVO.password)
        }

        val user = getUser(SecurityContextHolder.getContext().authentication)
        return ok(UserVO.buildFrom(user))
    }

    @PostMapping
    @RequestMapping("/logout")
    fun logout(@RequestBody userLogoutVO: UserLogoutVO) : ResponseEntity<Result> {
        val auth = SecurityContextHolder.getContext().authentication
        if (auth != null) {
            SecurityContextLogoutHandler().logout(request, response, auth)
        }
        if (rememberMeServices is RememberMeServiceImpl) {
            rememberMeServices.dropCookies(response)
        }
        return ok(Result("You are logged out!"))
    }


    @GetMapping
    @RequestMapping("/get_user_events/{email}")
    fun getUserEvents(@PathVariable("email") email: String): ResponseEntity<List<EventVO>>{
        return ok(userService.getUserEvents(email))
    }

    @PostMapping
    @RequestMapping("/join/{email}/{eventId}")
    fun addEvent(@PathVariable("email") email: String, @PathVariable("eventId") eventId: Long): ResponseEntity<Result>{
        userService.addEvent(email, eventId)
        return ok(Result("You subscribed to event!"))
    }


    fun rememberMeAuthenticate(username: String, password: String) {
        var newAuth = rememberMeServices.autoLogin(request, response)
        if (newAuth != null) {
            newAuth = authProvider.authenticate(newAuth)
            SecurityContextHolder.getContext().authentication = newAuth
        } else
            throw CookieNotFoundException("cookie not found")
    }

    fun authenticate(username: String, password: String) {
        val authToken = UsernamePasswordAuthenticationToken(username, password)
        val newAuth: Authentication = authProvider.authenticate(authToken)
        rememberMeServices.loginSuccess(request, response, newAuth)
        SecurityContextHolder.getContext().authentication = newAuth
    }

}