package com.example.afisha_api.repositories

import com.example.afisha_api.models.Event
import org.springframework.data.repository.CrudRepository

interface EventRepository: CrudRepository<Event, Long> {

}