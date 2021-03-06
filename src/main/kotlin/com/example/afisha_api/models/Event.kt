package com.example.afisha_api.models

import javax.persistence.*

@Entity
@Table(name = "event")
class Event(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        var id: Long,
        var title: String,
        var ageRestriction: Long,
        var description: String,
        var date: String,
        var numberParticipants: String,
        var posterUrl: String,
        var organizerEmail: String,
        @ManyToMany(mappedBy = "events")
        var participants: MutableList<User>? = null,
        var status: Status,
        @Lob
        @Column(name="event_picture")
        var eventPicture: ByteArray
) {
        enum class Status{
                APPROVED,
                NOT_APPROVED
        }
}

