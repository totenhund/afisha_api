package com.example.afisha_api.services

import com.example.afisha_api.models.OrganizerSubmission
import com.example.afisha_api.repositories.SubmissionRepository
import org.springframework.stereotype.Service

@Service
class SubmissionServiceImpl(val submissionRepository: SubmissionRepository): SubmissionService {
    override fun getAllSubmission(): List<OrganizerSubmission> {
        return submissionRepository.findAll()
    }

    override fun deleteSubmission(id: Long) {
        submissionRepository.deleteById(id)
    }
}