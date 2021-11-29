package com.example.afisha_api.services

import com.example.afisha_api.models.Event
import com.example.afisha_api.repositories.EventRepository
import org.springframework.stereotype.Service

@Service
class EventServiceImpl(val repository: EventRepository) : EventService {

    override fun addEvent(event: Event): Event {
        repository.save(event)
        return event
    }

    override fun getAllEvents(): List<Event> {
        return repository.findAll().toList()
    }

    override fun getEventById(id: Long): Event? {
        return repository.findById(id).orElse(null)
    }


}