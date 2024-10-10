package me.fortibrine.markers.marker

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

object Markers: Table() {
    val username: Column<String> = varchar("username", 5000)
    val markers: Column<String> = varchar("markers", 5000)

    override val primaryKey: PrimaryKey = PrimaryKey(username)
}