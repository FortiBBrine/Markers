package me.fortibrine.markers.commands

import dev.rollczi.litecommands.annotations.argument.Arg
import dev.rollczi.litecommands.annotations.command.Command
import dev.rollczi.litecommands.annotations.context.Context
import dev.rollczi.litecommands.annotations.execute.Execute
import dev.rollczi.litecommands.annotations.permission.Permission
import kotlinx.coroutines.*
import me.fortibrine.markers.configuration.Configuration
import me.fortibrine.markers.marker.MarkerManager
import org.bukkit.Bukkit
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import org.koin.core.annotation.Singleton

@Singleton
@Command(name = "marker")
@Permission("Markers.marker")
class MarkerCommand (
    private val markerManager: MarkerManager,
    private val configuration: Configuration,
    private val plugin: Plugin
) {

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    @Execute(name = "create")
    fun create(
        @Context player: Player,
        @Arg name: String,
    ) {
        scope.launch {
            val markers = markerManager.getMarkers(player.name).toMutableMap()
            markers[name] = player.location
            markerManager.setMarkers(player.name, markers)

            player.sendMessage(configuration.messages.createdMarker)
        }

    }

    @Execute(name = "read")
    fun read(
        @Context player: Player,
    ) {
        scope.launch {
            markerManager.getMarkers(player.name).forEach {

                val location = it.value

                Bukkit.getScheduler().runTask(
                    plugin,
                    Runnable {
                        val entity = location.world?.spawnEntity(location, EntityType.ARMOR_STAND) ?: return@Runnable
                        entity.isVisibleByDefault = false
                        entity.isCustomNameVisible = true
                        (entity as LivingEntity).setAI(false)
                        entity.isInvulnerable = true
                        entity.isGlowing = true
                        entity.customName = it.key

                        player.showEntity(plugin, entity)

                        Bukkit.getScheduler().runTaskLater(
                            plugin,
                            Runnable {
                                entity.remove()
                            },
                            5 * 20
                        )
                    }
                )
            }

            player.sendMessage(configuration.messages.readMarker)
        }

    }

    @Execute(name = "update")
    fun update(
        @Context player: Player,
        @Arg name: String,
    ) {
        scope.launch {
            val markers = markerManager.getMarkers(player.name).toMutableMap()
            markers[name] = player.location
            markerManager.setMarkers(player.name, markers)

            player.sendMessage(configuration.messages.updatedMarker)
        }

    }

    @Execute(name = "delete")
    fun delete(
        @Context player: Player,
        @Arg name: String,
    ) {
        scope.launch {
            val markers = markerManager.getMarkers(player.name).toMutableMap()
            markers.remove(name)
            markerManager.setMarkers(player.name, markers)

            player.sendMessage(configuration.messages.deletedMarker)
        }
    }

}