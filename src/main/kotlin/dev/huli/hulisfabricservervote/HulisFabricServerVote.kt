package dev.huli.hulisfabricservervote

import com.cobblemon.mod.common.platform.events.PlatformEvents
import com.cobblemon.mod.common.platform.events.ServerEvent
import com.mojang.brigadier.CommandDispatcher
import dev.huli.hulisfabricservervote.commands.ServerVoteCommand
import dev.huli.hulisfabricservervote.config.HulisFabricServerVoteConfig
import dev.huli.hulisfabricservervote.obj.ServerVoteThread
import dev.huli.hulisfabricservervote.permissions.ServerVotePermissions
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.server.MinecraftServer
import net.minecraft.server.command.ServerCommandSource
import java.io.File

object HulisFabricServerVote {
    var MOD_ID = "hulisfabricservervote"
    lateinit var SERVER : MinecraftServer
    val INSTANCE: HulisFabricServerVote = this
    lateinit var serverVoteThread: ServerVoteThread
    @Volatile var lastVoted = 0L

    lateinit var configDir:File
    lateinit var config:HulisFabricServerVoteConfig
    val permissions: ServerVotePermissions = ServerVotePermissions
    fun initialize(){
        permissions.invoke()
        CommandRegistrationCallback.EVENT.register(
            CommandRegistrationCallback { dispatcher, _, _ -> registerCommands(
                dispatcher
            )  }
        )
        PlatformEvents.SERVER_STARTED.subscribe { started: ServerEvent.Started -> run{
            this.SERVER = started.server
            reload()
            serverVoteThread = ServerVoteThread
        }  }
    }





    private fun initDirs(){
        configDir = File("${FabricLoader.getInstance().configDir}/HulisFabricServerVote/")
        configDir.mkdir()
    }

    private fun initConfigs(){
        HulisFabricServerVoteConfig().writeConfig()
        config = HulisFabricServerVoteConfig().getConfig()!!
        config.updateConfig()
    }

    fun reload(){
        initDirs()
        initConfigs()
    }

    private fun registerCommands(
        dispatcher: CommandDispatcher<ServerCommandSource>?
    ) {
        if (dispatcher != null) {
            ServerVoteCommand.register(dispatcher)
        }
    }
    fun <T : Enum<T>> Array<T>.names() = this.map { it.name }
}