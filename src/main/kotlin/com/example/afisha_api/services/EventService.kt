package com.example.afisha_api.services

import com.example.afisha_api.helpers.UserVO
import com.example.afisha_api.models.Event
import org.springframework.web.multipart.MultipartFile

interface EventService {
    fun addEvent(event: Event): Event

    fun getAllEvents(): List<Event>

    fun getOrganizerEvent(email: String): List<Event>

    fun getEventById(id: Long): Event?

    fun getApproved(): List<Event>

    fun getNotApproved(): List<Event>

    fun getEventParticipants(id: Long): List<UserVO>?

    fun deleteEvent(id: Long)

    fun setEventPoster(id: Long, file: MultipartFile)

    fun getEventPoster(id: Long): ByteArray
}