package com.example.afisha_api.helpers

import com.example.afisha_api.models.Event
import com.example.afisha_api.models.User

data class EventVO(
        var id: Long,
        var title: String,
        var ageRestriction: Long,
        var description: String,
        var date: String,
        var numberParticipants: String,
        var posterUrl: String
) {
    companion object {
        fun buildFrom(event: Event): EventVO {
            return EventVO(event.id, event.title, event.ageRestriction, event.description, event.date, event.numberParticipants, event.posterUrl)
        }
    }
}

data class UserLoginVO(
        var email: String,
        var password: String
)

data class UserLogoutVO(
        val email: String,
)

data class UserVO(
        var firstName: String,
        var secondName: String,
        var email: String,
        var password: String
) {
    companion object {
        fun buildFrom(user: User): UserVO {
            return UserVO(user.firstName, user.secondName, user.email, user.password)
        }
    }
}

data class Result(
        var msg: String
)