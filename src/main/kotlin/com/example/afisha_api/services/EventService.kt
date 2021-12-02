package com.example.afisha_api.services

import com.example.afisha_api.models.Event

interface EventService {
    fun addEvent(event: Event): Event

    fun getAllEvents(): List<Event>

    fun getEventById(id: Long): Event?

    fun getApproved(): List<Event>

    fun getNotApproved(): List<Event>
}