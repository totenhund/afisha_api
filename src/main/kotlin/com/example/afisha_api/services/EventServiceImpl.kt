package com.example.afisha_api.services

import com.example.afisha_api.helpers.UserVO
import com.example.afisha_api.models.Event
import com.example.afisha_api.repositories.EventRepository
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

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

    override fun getApproved(): List<Event> {
        return repository.findAll().toList().filter {
            it.status == Event.Status.APPROVED
        }
    }

    override fun getNotApproved(): List<Event> {
        return repository.findAll().toList().filter {
            it.status == Event.Status.NOT_APPROVED
        }
    }

    override fun getEventParticipants(id: Long): List<UserVO>? {
        return repository.findById(id).orElse(null).participants?.map {
            UserVO.buildFrom(it)
        }
    }

    override fun deleteEvent(id: Long) {
        repository.deleteById(id)
    }

    override fun setEventPoster(id: Long, file: MultipartFile) {
        val event = repository.findById(id).orElse(null)
        event?.let {
            it.eventPicture = file.bytes
            repository.save(it)
        }
    }

    override fun getEventPoster(id: Long): ByteArray {
        val event = repository.findById(id).orElse(null)
        return event.eventPicture
    }


}