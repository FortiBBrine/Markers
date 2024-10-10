package me.fortibrine.markers.configuration

import org.spongepowered.configurate.objectmapping.ConfigSerializable

@ConfigSerializable
data class Configuration (
    val jdbcUrl: String = "jdbc:sqlite:plugins/Markers/database.db",
    val messages: Messages = Messages()
)

@ConfigSerializable
data class Messages (
    val createdMarker: String = "§cВы успешно создали маркер.",
    val readMarker: String = "§cМаркеры успешно считаны.",
    val updatedMarker: String = "§cМаркеры успешно обновлены.",
    val deletedMarker: String = "§cМаркер успешно удалён.",
)
