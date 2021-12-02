package com.example.afisha_api.services

import com.example.afisha_api.exceptions.ForbiddenException
import com.example.afisha_api.helpers.EventVO
import com.example.afisha_api.helpers.UserVO
import com.example.afisha_api.models.Event
import com.example.afisha_api.models.OrganizerSubmission
import com.example.afisha_api.models.User
import com.example.afisha_api.repositories.EventRepository
import com.example.afisha_api.repositories.SubmissionRepository
import com.example.afisha_api.repositories.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserServiceImpl(
        private val repository: UserRepository,
        private val eventsRepository: EventRepository,
        private val submissionRepository: SubmissionRepository,
        private val passwordEncoder: PasswordEncoder
) : UserService {
    override fun existsEmail(email: String): Boolean {
        return repository.existsByEmail(email)
    }

    override fun createUser(user: UserVO) {
        if (existsEmail(user.email)) {
            throw ForbiddenException("User with such email already exists")
        } else {


            repository.saveAndFlush(User(
                    email = user.email,
                    firstName = user.firstName,
                    secondName = user.secondName,
                    password = passwordEncoder.encode(user.password),
                    role = User.Role.ROLE_USER,
                    events = mutableListOf()
            ))
        }
    }

    override fun addEvent(email: String, eventId: Long) {
        val event: Event? = eventsRepository.findById(eventId).orElse(null)

        val user = repository.findByEmail(email)

        if (user?.events?.contains(event) == false) {
            event?.let { user.events?.add(it) }
            repository.save(user)
        }
    }

    override fun getUserEvents(email: String): List<EventVO> {

        val userEvents = repository.findByEmail(email)?.events

        var events = mutableListOf<EventVO>()

        userEvents?.let {
            events = it.map { ev ->
                EventVO.buildFrom(ev)
            }.toMutableList()
        }

        return events

    }

    override fun applyOrganizer(email: String) {
        submissionRepository.save(OrganizerSubmission(userEmail = email))
    }

    override fun exitEvent(eventId: Long) {

    }

    override fun approveOrganizer(email: String) {
        val user = repository.findByEmail(email)
        user?.let {
            it.role = User.Role.ORGANIZER
            repository.save(it)
        }
    }

}