package dev.huli.cobblerandomizernuzlocke

import com.cobblemon.mod.common.Cobblemon
import com.cobblemon.mod.common.api.events.CobblemonEvents
import com.cobblemon.mod.common.api.events.pokeball.PokeBallCaptureCalculatedEvent
import com.cobblemon.mod.common.api.events.pokemon.PokemonFaintedEvent
import com.cobblemon.mod.common.platform.events.PlatformEvents
import com.cobblemon.mod.common.platform.events.ServerEvent
import com.mojang.brigadier.CommandDispatcher
import dev.huli.cobblerandomizernuzlocke.commands.ToggleNuzlocke
import dev.huli.cobblerandomizernuzlocke.commands.ToggleRandomizer
import dev.huli.cobblerandomizernuzlocke.config.CobbleRandomizerConfig
import dev.huli.cobblerandomizernuzlocke.util.RandomizerUtil
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.server.MinecraftServer
import net.minecraft.server.command.ServerCommandSource
import java.io.File

object CobbleRandomizerNuzlocke {
    var MOD_ID = "cobblerandomizernuzlocke"
    private lateinit var SERVER : MinecraftServer
    val INSTANCE: CobbleRandomizerNuzlocke = this

    lateinit var config:CobbleRandomizerConfig
    lateinit var configDir:File
    fun initialize(){
        CommandRegistrationCallback.EVENT.register(
            CommandRegistrationCallback { dispatcher, _, _ -> registerCommands(
                dispatcher
            )  }
        )
        PlatformEvents.SERVER_STARTED.subscribe { started: ServerEvent.Started -> run{
            this.SERVER = started.server
            reload()
        }  }

        CobblemonEvents.POKE_BALL_CAPTURE_CALCULATED.subscribe { pokeballCaptureCalculated: PokeBallCaptureCalculatedEvent -> pokemonCaught(pokeballCaptureCalculated) }
        CobblemonEvents.POKEMON_FAINTED.subscribe { pokemonFaintedEvent : PokemonFaintedEvent -> pokemonFainted(pokemonFaintedEvent) }
    }


    private fun initDirs(){
        configDir = File("${FabricLoader.getInstance().configDir}/CobbleRandomizerNuzlocke/")
        configDir.mkdir()
    }

    private fun initConfigs(){
        CobbleRandomizerConfig().writeConfig()
        config = CobbleRandomizerConfig().getConfig()!!
    }

    fun reload(){
        initDirs()
        initConfigs()
    }

    private fun registerCommands(
        dispatcher: CommandDispatcher<ServerCommandSource>?
    ) {
        if (dispatcher != null) {
            ToggleNuzlocke.register(dispatcher)
            ToggleRandomizer.register(dispatcher)
        }
    }

    private fun pokemonCaught(event: PokeBallCaptureCalculatedEvent) {
        if(config.randomizerDataManager.randomizerOn){
            if(event.captureResult.isSuccessfulCapture){
            event.pokemonEntity.pokemon = RandomizerUtil.getRandomPokemon(event.pokemonEntity.pokemon)
            }
        }
    }

    private fun pokemonFainted(event: PokemonFaintedEvent) {
        if(config.randomizerDataManager.nuzlockeOn){
            event.pokemon.getOwnerPlayer()?.let {
                val partyStore = Cobblemon.storage.getParty(it)
                partyStore.remove(event.pokemon)
                }
        }
    }

}