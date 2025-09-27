package com.match.matchmate.data.mapper

import com.match.matchmate.data.local.entity.MatchMateEntity
import com.match.matchmate.data.model.MatchMateDto
import com.match.matchmate.data.model.MatchStatus

fun MatchMateDto.Result.toEntity(): MatchMateEntity {
    return MatchMateEntity(
        uuid = login.uuid,
        cell = cell,
        dobAge = dob.age,
        dobDate = dob.date,
        email = email,
        gender = gender,
        idName = id.name,
        idValue = id.value,
        locationCity = location.city,
        locationCoordinatesLatitude = location.coordinates.latitude,
        locationCoordinatesLongitude = location.coordinates.longitude,
        locationCountry = location.country,
        locationState = location.state,
        locationStreetName = location.street.name,
        locationStreetNumber = location.street.number,
        locationTimezoneDescription = location.timezone.description,
        locationTimezoneOffset = location.timezone.offset,
        loginMd5 = login.md5,
        loginPassword = login.password,
        loginSalt = login.salt,
        loginSha1 = login.sha1,
        loginSha256 = login.sha256,
        loginUsername = login.username,
        loginUuid = login.uuid,
        nameFirst = name.first,
        nameLast = name.last,
        nameTitle = name.title,
        nat = nat,
        phone = phone,
        pictureLarge = picture.large,
        pictureMedium = picture.medium,
        pictureThumbnail = picture.thumbnail,
        registeredAge = registered.age,
        registeredDate = registered.date,
        matchStatus = matchStatus
    )
}


fun MatchMateEntity.toResult(): MatchMateDto.Result {
    return MatchMateDto.Result(
        cell = cell,
        dob = MatchMateDto.Result.Dob(
            age = dobAge,
            date = dobDate
        ),
        email = email,
        gender = gender,
        id = MatchMateDto.Result.Id(
            name = idName,
            value = idValue
        ),
        location = MatchMateDto.Result.Location(
            city = locationCity,
            coordinates = MatchMateDto.Result.Location.Coordinates(
                latitude = locationCoordinatesLatitude,
                longitude = locationCoordinatesLongitude
            ),
            country = locationCountry,
            state = locationState,
            street = MatchMateDto.Result.Location.Street(
                name = locationStreetName,
                number = locationStreetNumber
            ),
            timezone = MatchMateDto.Result.Location.Timezone(
                description = locationTimezoneDescription,
                offset = locationTimezoneOffset
            )
        ),
        login = MatchMateDto.Result.Login(
            md5 = loginMd5,
            password = loginPassword,
            salt = loginSalt,
            sha1 = loginSha1,
            sha256 = loginSha256,
            username = loginUsername,
            uuid = loginUuid
        ),
        name = MatchMateDto.Result.Name(
            first = nameFirst,
            last = nameLast,
            title = nameTitle
        ),
        nat = nat,
        phone = phone,
        picture = MatchMateDto.Result.Picture(
            large = pictureLarge,
            medium = pictureMedium,
            thumbnail = pictureThumbnail
        ),
        registered = MatchMateDto.Result.Registered(
            age = registeredAge,
            date = registeredDate
        ),
        matchStatus = matchStatus
    )
}

fun List<MatchMateDto.Result>.toEntityList(): List<MatchMateEntity> {
    return map { it.toEntity() }
}

fun List<MatchMateEntity>.toResultList(): List<MatchMateDto.Result> {
    return map { it.toResult() }
}