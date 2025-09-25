package com.match.matchmate.data.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MatchMateDto(
    @SerialName("info")
    val info: Info = Info(),
    @SerialName("results")
    val results: List<Result> = listOf()
) {
    @Serializable
    data class Info(
        @SerialName("page")
        val page: Int = 0,
        @SerialName("results")
        val results: Int = 0,
        @SerialName("seed")
        val seed: String = "",
        @SerialName("version")
        val version: String = ""
    )

    @Serializable
    data class Result(
        @SerialName("cell")
        val cell: String = "",
        @SerialName("dob")
        val dob: Dob = Dob(),
        @SerialName("email")
        val email: String = "",
        @SerialName("gender")
        val gender: String = "",
        @SerialName("id")
        val id: Id = Id(),
        @SerialName("location")
        val location: Location = Location(),
        @SerialName("login")
        val login: Login = Login(),
        @SerialName("name")
        val name: Name = Name(),
        @SerialName("nat")
        val nat: String = "",
        @SerialName("phone")
        val phone: String = "",
        @SerialName("picture")
        val picture: Picture = Picture(),
        @SerialName("registered")
        val registered: Registered = Registered(),

        val matchStatus: MatchStatus = MatchStatus.NOT_DECIDED
    ) {
        @Serializable
        data class Dob(
            @SerialName("age")
            val age: Int = 0,
            @SerialName("date")
            val date: String = ""
        )

        @Serializable
        data class Id(
            @SerialName("name")
            val name: String = "",
            @SerialName("value")
            val value: String? = ""
        )

        @Serializable
        data class Location(
            @SerialName("city")
            val city: String = "",
            @SerialName("coordinates")
            val coordinates: Coordinates = Coordinates(),
            @SerialName("country")
            val country: String = "",
            @SerialName("state")
            val state: String = "",
            @SerialName("street")
            val street: Street = Street(),
            @SerialName("timezone")
            val timezone: Timezone = Timezone()
        ) {
            @Serializable
            data class Coordinates(
                @SerialName("latitude")
                val latitude: String = "",
                @SerialName("longitude")
                val longitude: String = ""
            )

            @Serializable
            data class Street(
                @SerialName("name")
                val name: String = "",
                @SerialName("number")
                val number: Int = 0
            )

            @Serializable
            data class Timezone(
                @SerialName("description")
                val description: String = "",
                @SerialName("offset")
                val offset: String = ""
            )
        }

        @Serializable
        data class Login(
            @SerialName("md5")
            val md5: String = "",
            @SerialName("password")
            val password: String = "",
            @SerialName("salt")
            val salt: String = "",
            @SerialName("sha1")
            val sha1: String = "",
            @SerialName("sha256")
            val sha256: String = "",
            @SerialName("username")
            val username: String = "",
            @SerialName("uuid")
            val uuid: String = ""
        )

        @Serializable
        data class Name(
            @SerialName("first")
            val first: String = "",
            @SerialName("last")
            val last: String = "",
            @SerialName("title")
            val title: String = ""
        )

        @Serializable
        data class Picture(
            @SerialName("large")
            val large: String = "",
            @SerialName("medium")
            val medium: String = "",
            @SerialName("thumbnail")
            val thumbnail: String = ""
        )

        @Serializable
        data class Registered(
            @SerialName("age")
            val age: Int = 0,
            @SerialName("date")
            val date: String = ""
        )
    }
}

enum class MatchStatus {
    LIKED,
    DISLIKED,
    NOT_DECIDED
}