package me.fortibrine.markers

import dev.rollczi.litecommands.LiteCommands
import dev.rollczi.litecommands.bukkit.LiteBukkitFactory
import me.fortibrine.markers.commands.MarkerCommand
import me.fortibrine.markers.configuration.Configuration
import me.fortibrine.markers.module.PluginModule
import org.bukkit.command.CommandSender
import org.bukkit.event.Listener
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import org.koin.java.KoinJavaComponent.getKoin
import org.koin.ksp.generated.module
import org.spongepowered.configurate.gson.GsonConfigurationLoader
import java.io.File

class MarkersPlugin: JavaPlugin() {

    private lateinit var liteCommands: LiteCommands<CommandSender>

    override fun onEnable() {

        val loader = GsonConfigurationLoader.builder()
            .path(File(dataFolder, "config.json").toPath())
            .build()

        val node = loader.load()
        val config: Configuration = node.get(Configuration::class.java) ?: Configuration()
        node.set(config)
        loader.save(node)

        startKoin {
            modules(
                module {
                    single<Plugin> { this@MarkersPlugin }
                    single { config }
                    single { loader }
                    single { node }
                },
                PluginModule().module
            )
        }

        val listeners: List<Listener> = getKoin().getAll()

        listeners.forEach { listener ->
            this.server.pluginManager.registerEvents(listener, this@MarkersPlugin)
        }

        liteCommands = LiteBukkitFactory.builder("markers", this)
            .commands(getKoin().get<MarkerCommand>())
            .build()

    }

    override fun onDisable() {
        stopKoin()
        liteCommands.unregister()
    }

}