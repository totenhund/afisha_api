package com.example.afisha_api.controlles

import com.example.afisha_api.exceptions.CookieNotFoundException
import com.example.afisha_api.helpers.*
import com.example.afisha_api.models.Event
import com.example.afisha_api.models.OrganizerSubmission
import com.example.afisha_api.security.RememberMeServiceImpl
import com.example.afisha_api.services.EventService
import com.example.afisha_api.services.SubmissionService
import com.example.afisha_api.services.UserService
import com.example.afisha_api.utils.SecurityUtil.Companion.getUser
import com.example.afisha_api.utils.Validator
import io.swagger.annotations.Api
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
@Api(value = "/api", description = "User operations")
class UserController(
    private val userService: UserService,
    private val eventService: EventService,
    private val authProvider: AuthenticationProvider,
    private val validator: Validator,
    private val rememberMeServices: RememberMeServices,
    private val submissionService: SubmissionService
) {

    @Autowired
    private lateinit var request: HttpServletRequest

    @Autowired
    private lateinit var response: HttpServletResponse

    @PostMapping
    @RequestMapping("/register", method = [RequestMethod.POST])
    fun register(@RequestBody user: UserVO): ResponseEntity<Result> {
        validator.validateEmail(user.email)
        userService.createUser(user)
        return ok(Result("You are registered!"))
    }

    @PostMapping
    @RequestMapping("/login", method = [RequestMethod.POST])
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
    @RequestMapping("/logout", method = [RequestMethod.POST])
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
    @RequestMapping("/get_user_events/{email}", method = [RequestMethod.GET])
    fun getUserEvents(@PathVariable("email") email: String): ResponseEntity<List<EventVO>>{
        return ok(userService.getUserEvents(email))
    }

    @PostMapping
    @RequestMapping("/join/{email}/{event_id}", method = [RequestMethod.POST])
    fun addEvent(@PathVariable("email") email: String, @PathVariable("event_id") eventId: Long): ResponseEntity<Result>{
        userService.addEvent(email, eventId)
        return ok(Result("You subscribed to event!"))
    }

    @PostMapping
    @RequestMapping("/apply_for_organizer/{email}", method = [RequestMethod.POST])
    fun applyOrganizer(@PathVariable("email") email: String): ResponseEntity<Result>{
        userService.applyOrganizer(email)
        return ok(Result("You applied to be organizer"))
    }

    @PostMapping
    @RequestMapping("/approve_event/{event_id}", method = [RequestMethod.POST])
    fun submitEvent(@PathVariable("event_id") eventId: Long): ResponseEntity<Result>{
        val event = eventService.getEventById(eventId)
        event?.let {
            it.status = Event.Status.APPROVED
            eventService.addEvent(it)
        }
        return ok(Result("You approved event"))
    }

    @PostMapping
    @RequestMapping("/approve_organizer/{email}/{submission_id}", method = [RequestMethod.POST])
    fun approveOrganizer(@PathVariable("email") email: String, @PathVariable("submission_id") id: Long): ResponseEntity<Result>{
        userService.approveOrganizer(email)
        submissionService.deleteSubmission(id)
        return ok(Result("You approved organizer"))
    }

    @PostMapping
    @RequestMapping("/cancel_organizer/{submission_id}", method = [RequestMethod.POST])
    fun cancelOrganizerSubmission(@PathVariable("submission_id") id: Long): ResponseEntity<Result>{
        submissionService.deleteSubmission(id)
        return ok(Result("You canceled organizer"))
    }

    @PostMapping
    @RequestMapping("/cancel_event/{event_id}", method = [RequestMethod.POST])
    fun cancelEventSubmission(@PathVariable("event_id") eventId: Long) : ResponseEntity<Result>{
        eventService.deleteEvent(eventId)
        return ok(Result("You canceled event"))
    }

    @GetMapping
    @RequestMapping("/all_submissions", method = [RequestMethod.GET])
    fun getSubmission(): ResponseEntity<List<OrganizerSubmission>>{
        return ok(submissionService.getAllSubmission())
    }

    @PostMapping
    @RequestMapping("/{event_id}/{email}/exit", method = [RequestMethod.POST])
    fun exitEvent(@PathVariable("event_id") eventId: Long, @PathVariable("email") email: String): ResponseEntity<Result>{
        userService.exitEvent(eventId, email)
        return ok(Result("You exit event"))
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