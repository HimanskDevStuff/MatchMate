package com.match.matchmate.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.match.matchmate.data.model.MatchStatus

@Entity(tableName = "match_mate_results")
data class MatchMateEntity(
    @PrimaryKey
    @ColumnInfo(name = "uuid")
    val uuid: String,
    @ColumnInfo(name = "cell")
    val cell: String,
    @ColumnInfo(name = "dob_age")
    val dobAge: Int,
    @ColumnInfo(name = "dob_date")
    val dobDate: String,
    @ColumnInfo(name = "email")
    val email: String,
    @ColumnInfo(name = "gender")
    val gender: String,
    @ColumnInfo(name = "id_name")
    val idName: String,
    @ColumnInfo(name = "id_value")
    val idValue: String?,
    @ColumnInfo(name = "location_city")
    val locationCity: String,
    @ColumnInfo(name = "location_coordinates_latitude")
    val locationCoordinatesLatitude: String,
    @ColumnInfo(name = "location_coordinates_longitude")
    val locationCoordinatesLongitude: String,
    @ColumnInfo(name = "location_country")
    val locationCountry: String,
    @ColumnInfo(name = "location_state")
    val locationState: String,
    @ColumnInfo(name = "location_street_name")
    val locationStreetName: String,
    @ColumnInfo(name = "location_street_number")
    val locationStreetNumber: Int,
    @ColumnInfo(name = "location_timezone_description")
    val locationTimezoneDescription: String,
    @ColumnInfo(name = "location_timezone_offset")
    val locationTimezoneOffset: String,
    @ColumnInfo(name = "login_md5")
    val loginMd5: String,
    @ColumnInfo(name = "login_password")
    val loginPassword: String,
    @ColumnInfo(name = "login_salt")
    val loginSalt: String,
    @ColumnInfo(name = "login_sha1")
    val loginSha1: String,
    @ColumnInfo(name = "login_sha256")
    val loginSha256: String,
    @ColumnInfo(name = "login_username")
    val loginUsername: String,
    @ColumnInfo(name = "login_uuid")
    val loginUuid: String,
    @ColumnInfo(name = "name_first")
    val nameFirst: String,
    @ColumnInfo(name = "name_last")
    val nameLast: String,
    @ColumnInfo(name = "name_title")
    val nameTitle: String,
    @ColumnInfo(name = "nat")
    val nat: String,
    @ColumnInfo(name = "phone")
    val phone: String,
    @ColumnInfo(name = "picture_large")
    val pictureLarge: String,
    @ColumnInfo(name = "picture_medium")
    val pictureMedium: String,
    @ColumnInfo(name = "picture_thumbnail")
    val pictureThumbnail: String,
    @ColumnInfo(name = "registered_age")
    val registeredAge: Int,
    @ColumnInfo(name = "registered_date")
    val registeredDate: String,
    @ColumnInfo(name = "match_status")
    val matchStatus: MatchStatus,
    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis()
)