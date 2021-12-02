package com.example.afisha_api.repositories

import com.example.afisha_api.models.OrganizerSubmission
import org.springframework.data.jpa.repository.JpaRepository

interface SubmissionRepository: JpaRepository<OrganizerSubmission, Long> {

}