package dev.huli.cobblerandomizernuzlocke.commands

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.BoolArgumentType
import com.mojang.brigadier.context.CommandContext
import dev.huli.cobblerandomizernuzlocke.CobbleRandomizerNuzlocke
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.text.Text

object ToggleRandomizerChaosMode {

    fun register(dispatcher:CommandDispatcher<ServerCommandSource>){
        println("Trying to register Command togglerandomizer")
    dispatcher.register(CommandManager.literal("togglerandomizerchaos")
        .executes { toggleNoArg(it) }
        .then(
            CommandManager.argument("state",BoolArgumentType.bool())
                .executes { toggleArg(it) }
        ))

    }

    private fun toggleNoArg(ctx:CommandContext<ServerCommandSource>): Int {
        toggle(ctx,!CobbleRandomizerNuzlocke.INSTANCE.config.randomizerDataManager.randomizerChaosOn)
        return 1
    }

    private fun toggleArg(ctx: CommandContext<ServerCommandSource>):Int{
        val arg = BoolArgumentType.getBool(ctx,"state")
        toggle(ctx,arg)
        return 1
    }

    private fun toggle(ctx: CommandContext<ServerCommandSource>, toggle: Boolean){
        CobbleRandomizerNuzlocke.INSTANCE.config.randomizerDataManager.randomizerChaosOn = toggle
        ctx.source.sendMessage(Text.literal("Toggled Randomizer Chaos Mode to $toggle"))
        CobbleRandomizerNuzlocke.INSTANCE.config.updateConfig()
    }
}