package com.example.afisha_api.controlles

import com.example.afisha_api.components.EventAssembler
import com.example.afisha_api.helpers.EventVO
import com.example.afisha_api.helpers.Result
import com.example.afisha_api.helpers.UserVO
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
    @RequestMapping("/{event_id}", method = [RequestMethod.GET])
    fun getEvent(@PathVariable("event_id") eventId: Long): ResponseEntity<EventVO> {
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

    @PostMapping
    @RequestMapping("/{event_id}", method = [RequestMethod.POST])
    fun deleteEvent(@PathVariable("event_id") eventId: Long): ResponseEntity<Result>{
        eventService.deleteEvent(eventId)
        return ResponseEntity.ok(Result("Event deleted"))
    }

    @GetMapping
    @RequestMapping("/{event_id}/participants", method = [RequestMethod.GET])
    fun getParticipants(@PathVariable("event_id") eventId: Long): ResponseEntity<List<UserVO>>{

        val participants = eventService.getEventParticipants(eventId)

        return if (participants == null){
            ResponseEntity.ok(
                    mutableListOf()
            )
        } else {
            ResponseEntity.ok(
                    participants
            )
        }
    }

}