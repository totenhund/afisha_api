package com.example.afisha_api.controlles

import com.example.afisha_api.components.EventAssembler
import com.example.afisha_api.helpers.EventVO
import com.example.afisha_api.models.Event
import com.example.afisha_api.services.EventService
import io.swagger.annotations.Api
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/events")
@Api(value = "/api", description = "Event operations")
class EventController(val eventService: EventService, val eventAssembler: EventAssembler) {

    @GetMapping
    @RequestMapping("/get_all", method = [RequestMethod.GET])
    fun getAllEvents(): ResponseEntity<List<EventVO>> {
        return ResponseEntity.ok(eventService.getAllEvents().map {
            eventAssembler.toEventVO(it)
        })
    }

    @PostMapping
    @RequestMapping("/add_event", method = [RequestMethod.POST])
    fun addEvent(@Validated @RequestBody event: Event): ResponseEntity<EventVO> {
        return ResponseEntity.ok(eventService.addEvent(event).let {
            eventAssembler.toEventVO(it)
        })
    }

    @GetMapping
    @RequestMapping("/{eventId}", method = [RequestMethod.GET])
    fun getEvent(@PathVariable("eventId") eventId: Long): ResponseEntity<EventVO> {
        return ResponseEntity.ok(eventService.getEventById(eventId)?.let { eventAssembler.toEventVO(it) })
    }

    @GetMapping
    @RequestMapping("/get_approved", method = [RequestMethod.GET])
    fun approvedEvents(): ResponseEntity<List<EventVO>>{
        return ResponseEntity.ok(eventService.getApproved().map {
            eventAssembler.toEventVO(it)
        })
    }

    @GetMapping
    @RequestMapping("/get_not_approved", method = [RequestMethod.GET])
    fun notApprovedEvents(): ResponseEntity<List<EventVO>>{
        return ResponseEntity.ok(eventService.getNotApproved().map {
            eventAssembler.toEventVO(it)
        })
    }

}