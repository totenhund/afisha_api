package com.example.afisha_api.components

import com.example.afisha_api.helpers.EventVO
import com.example.afisha_api.models.Event
import org.springframework.stereotype.Component

@Component
class EventAssembler {
    fun toEventVO(event: Event): EventVO {
        return EventVO(
                event.id,
                event.title,
                event.ageRestriction,
                event.description,
                event.date,
                event.numberParticipants,
                event.posterUrl
        )
    }
}