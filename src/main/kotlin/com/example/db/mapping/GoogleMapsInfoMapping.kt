package com.example.db.mapping

import com.example.model.entity.GoogleMapsInfo
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object GoogleMapsInfoTable : IntIdTable("google_maps_info") {
    val storeId = reference("storeid", StoreTable)

    val placeId = varchar("placeid", 255)
    val phoneNumber = varchar("phone_number", 50).nullable()
    val websiteUri = text("website_uri").nullable()
    val googleMapsUri = text("google_maps_uri").nullable()

    val rating = double("rating").nullable()
    val userRatingCount = integer("user_rating_count").nullable()

    val hasParking = bool("has_parking")
    val wheelchairAccessible = bool("wheelchair_accessible")
}

class GoogleMapsInfoDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<GoogleMapsInfoDAO>(GoogleMapsInfoTable)

    var store by StoreDAO referencedOn GoogleMapsInfoTable.storeId

    var placeId by GoogleMapsInfoTable.placeId
    var phoneNumber by GoogleMapsInfoTable.phoneNumber
    var websiteUri by GoogleMapsInfoTable.websiteUri
    var googleMapsUri by GoogleMapsInfoTable.googleMapsUri

    var rating by GoogleMapsInfoTable.rating
    var userRatingCount by GoogleMapsInfoTable.userRatingCount

    var hasParking by GoogleMapsInfoTable.hasParking
    var wheelchairAccessible by GoogleMapsInfoTable.wheelchairAccessible
}

fun googleMapsDaoToModel(dao: GoogleMapsInfoDAO) = GoogleMapsInfo(
    dao.id.value,
    dao.store.id.value,
    dao.placeId,
    dao.phoneNumber,
    dao.websiteUri,
    dao.googleMapsUri,
    dao.rating,
    dao.userRatingCount,
    dao.hasParking,
    dao.wheelchairAccessible
)