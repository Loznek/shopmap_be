package com.example.model.mapping

import AppUser
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object AppUserTable : IntIdTable("appuser") {

    val firebaseUid = text("firebaseuid").uniqueIndex()
    val email = text("email").nullable()
    val displayName = text("displayname").nullable()
}

class AppUserDAO(id: EntityID<Int>) : IntEntity(id) {

    companion object : IntEntityClass<AppUserDAO>(AppUserTable)

    var firebaseUid by AppUserTable.firebaseUid
    var email by AppUserTable.email
    var displayName by AppUserTable.displayName
}

fun daoToModel(dao: AppUserDAO): AppUser =
    AppUser(
        id = dao.id.value,
        firebaseUid = dao.firebaseUid,
        email = dao.email,
        displayName = dao.displayName
    )

