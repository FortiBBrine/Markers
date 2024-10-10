package me.fortibrine.markers.marker

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import me.fortibrine.markers.configuration.Configuration
import org.bukkit.Bukkit
import org.bukkit.Location
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.core.annotation.Singleton

@Singleton
class MarkerManager (
    configuration: Configuration
) {

    init {
        Database.connect(
            configuration.jdbcUrl
        )

        transaction {
            SchemaUtils.create(Markers)
        }
    }

    fun getMarkers(username: String): Map<String, Location> =
        transaction {
            val result = Markers.selectAll().where { Markers.username eq username }
            if (result.count() < 1) {
                return@transaction emptyMap()
            }

            val markers = result.single()[Markers.markers]

            return@transaction Gson().fromJson<Map<String, Position>>(
                markers, object : TypeToken<Map<String, Position>>() {}.type
            ).mapValues {
                Location(Bukkit.getWorld(it.value.world), it.value.x, it.value.y, it.value.z)
            }
        }

    fun setMarkers(username: String, markers: Map<String, Location>): Unit =
        transaction {
            val positions = markers.mapValues { Position(it.value.x, it.value.y, it.value.z, it.value.world?.name ?: "world") }

            if (Markers.selectAll().where { Markers.username eq username }.count() > 0) {
                Markers.update({ Markers.username eq username }) {
                    it[Markers.username] = username
                    it[Markers.markers] = Gson().toJson(positions)
                }
                return@transaction
            }

            Markers.insert {
                it[Markers.username] = username
                it[Markers.markers] = Gson().toJson(positions)
            }

        }

    class Position (
        val x: Double,
        val y: Double,
        val z: Double,
        val world: String
    )

}
