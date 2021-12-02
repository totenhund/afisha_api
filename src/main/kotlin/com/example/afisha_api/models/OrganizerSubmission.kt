package com.example.afisha_api.models

import javax.persistence.*

@Entity
@Table(name = "organizer_submission")
class OrganizerSubmission(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0,
    var userEmail: String
)