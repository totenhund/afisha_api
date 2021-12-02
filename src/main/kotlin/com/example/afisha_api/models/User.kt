package com.example.afisha_api.models

import javax.persistence.*

@Entity
@Table(name = "users")
class User(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        var id: Long = 0,
        var firstName: String,
        var secondName: String,
        var email: String,
        var password: String,
        @Enumerated(EnumType.STRING)
        var role: Role = Role.ROLE_USER,

        @ManyToMany(fetch = FetchType.EAGER)
        @JoinTable(name = "user_events",
                joinColumns = [JoinColumn(name = "user_id")],
                inverseJoinColumns = [JoinColumn(name = "role_id")]
        )
        var events: MutableList<Event>? = null
) {

    enum class Role {
        ROLE_USER,
        ORGANIZER,
        ADMIN
    }
}