package com.example.afisha_api.services

import com.example.afisha_api.models.OrganizerSubmission

interface SubmissionService {

    fun getAllSubmission(): List<OrganizerSubmission>

    fun deleteSubmission(id: Long)

}