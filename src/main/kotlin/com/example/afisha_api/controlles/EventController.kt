package com.example.afisha_api.controlles

import com.example.afisha_api.components.EventAssembler
import com.example.afisha_api.helpers.EventVO
import com.example.afisha_api.models.Event
import com.example.afisha_api.services.EventServiceImpl
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/events")
class EventController(val eventService: EventServiceImpl, val eventAssembler: EventAssembler) {

    @GetMapping
    @RequestMapping("/get_all")
    fun getAllEvents(): ResponseEntity<List<EventVO>> {
        return ResponseEntity.ok(eventService.getAllEvents().map {
            eventAssembler.toEventVO(it)
        })
    }

    @PostMapping
    @RequestMapping("/add_event")
    fun addEvent(@Validated @RequestBody event: Event): ResponseEntity<EventVO> {
        return ResponseEntity.ok(eventService.addEvent(event).let {
            eventAssembler.toEventVO(it)
        })
    }

    @GetMapping
    @RequestMapping("/{eventId}")
    fun getEvent(@PathVariable("eventId") eventId: Long): ResponseEntity<EventVO> {
        return ResponseEntity.ok(eventService.getEventById(eventId)?.let { eventAssembler.toEventVO(it) })
    }

}