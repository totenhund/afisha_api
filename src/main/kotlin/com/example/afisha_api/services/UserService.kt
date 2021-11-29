package com.example.afisha_api.services

import com.example.afisha_api.helpers.EventVO
import com.example.afisha_api.helpers.UserVO

interface UserService {

    fun existsEmail(email: String): Boolean

    fun createUser(user: UserVO)

    fun addEvent(email: String, eventId: Long)

    fun getUserEvents(email: String): List<EventVO>

}