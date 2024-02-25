package dev.huli.hulisfabricservervote.commands

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import dev.huli.hulisfabricservervote.HulisFabricServerVote
import dev.huli.hulisfabricservervote.enums.SkipVoteGoals
import dev.huli.hulisfabricservervote.enums.TimeVoteGoals
import dev.huli.hulisfabricservervote.enums.VoteTypes
import dev.huli.hulisfabricservervote.enums.WeatherVoteGoals
import dev.huli.hulisfabricservervote.permissions.ServerVotePermissions
import dev.huli.hulisfabricservervote.util.TextUtil
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource

object ServerVoteCommand {

    fun register(dispatcher:CommandDispatcher<ServerCommandSource>){
        println("Trying to register Command servervote")
    dispatcher.register(CommandManager.literal("servervote")
        .then(CommandManager.literal("skip")
            .then(
                CommandManager.argument("votegoal",StringArgumentType.string())
                    .suggests { _: CommandContext<ServerCommandSource>, builder: SuggestionsBuilder ->
                        run{
                            SkipVoteGoals.entries.forEach {
                                builder.suggest(it.name)
                            }
                            builder.buildFuture()
                        }
                    }
                    .executes { executeSkipVote(it) }
            )
        )
        .then(CommandManager.literal("weather")
            .then(
                CommandManager.argument("votegoal",StringArgumentType.string())
                    .suggests { _: CommandContext<ServerCommandSource>, builder: SuggestionsBuilder ->
                        run{
                            WeatherVoteGoals.entries.forEach {
                                builder.suggest(it.name)
                            }
                            builder.buildFuture()
                        }
                    }
                    .executes { executeWeatherVote(it) }
            )
        )
        .then(CommandManager.literal("time")
            .then(
                CommandManager.argument("votegoalInt",IntegerArgumentType.integer(0,24000))
                    .executes { executeTimeVote(it) }
            )
            .then(
                CommandManager.argument("votegoal",StringArgumentType.string())
                    .suggests { _: CommandContext<ServerCommandSource>, builder: SuggestionsBuilder ->
                        run{
                            TimeVoteGoals.entries.forEach {
                                builder.suggest(it.name)
                            }
                            builder.buildFuture()
                        }
                    }
                    .executes { executeTimeVote(it) }
            )
        )
        .then(CommandManager.literal("vote")
            .then(CommandManager.literal("yes")
                .executes { voteYes(it) }
            )
            .then(CommandManager.literal("no")
                .executes { voteNo(it) }
            )
        )
        .then(CommandManager.literal("reload")
            .requires{src->ServerVotePermissions.checkPermission(src, HulisFabricServerVote.permissions.RELOAD_PERMISSION)}
            .executes { reload(it) }
        )

    )


    }

    private fun reload(ctx: CommandContext<ServerCommandSource>?): Int {
        HulisFabricServerVote.INSTANCE.reload()
        ctx?.source?.player?.sendMessage(TextUtil.getPrefixText("Config reloaded"))
        return 1
    }

    private fun executeTimeVote(ctx: CommandContext<ServerCommandSource>?): Int {

        if(HulisFabricServerVote.SERVER.playerManager.playerList.size < HulisFabricServerVote.config.hulisFabricServerVoteDataManager.minimumPlayersNeededToVote){
            ctx?.source?.player?.sendMessage(TextUtil.getVoteNotEnoughPlayersText())
            return 1
        }

        if(HulisFabricServerVote.serverVoteThread.isRunning){
            sendVoteRunningMessage(ctx)
            return 1
        }
        if(!isVoteCooldownOver()){
            ctx?.source?.player?.sendMessage(TextUtil.getVoteCooldownText())
            return 1
        }
        var goal = -1
        try {
            // Try to get the goal from the integer argument first
            goal = IntegerArgumentType.getInteger(ctx,"votegoalInt")
        } catch (_: IllegalArgumentException){
        }
        try {
            // If the integer argument failed, try to get the goal from the string argument
            goal = TimeVoteGoals.valueOf(StringArgumentType.getString(ctx,"votegoal")).weatherGoal
        } catch (_: IllegalArgumentException){

        }
         if (goal in 0..24000){
             // If the goal is valid, start the vote
             try {
                 // Try to get the goal from the integer argument first
                 ctx?.source?.server?.sendMessage(TextUtil.getVoteStartedText(ctx, null, TimeVoteGoals.valueOf(StringArgumentType.getString(ctx,"votegoal")),null, null, VoteTypes.TIME))
             }
             catch (e: IllegalArgumentException){
                    // If the integer argument failed, try to get the goal from the string argument
                    ctx?.source?.server?.sendMessage(TextUtil.getVoteStartedText(ctx, null, null,goal, null, VoteTypes.TIME))
             }
            executeTime(goal)
            return 1
        }
        return Int.MIN_VALUE
    }

    private fun executeTime(goal: Int) {
        HulisFabricServerVote.serverVoteThread.voteType = VoteTypes.TIME
        HulisFabricServerVote.serverVoteThread.timeVoteGoal = goal
        HulisFabricServerVote.serverVoteThread.startVote()

    }

    private fun executeWeatherVote(ctx: CommandContext<ServerCommandSource>?): Int {
        if(HulisFabricServerVote.SERVER.playerManager.playerList.size < HulisFabricServerVote.config.hulisFabricServerVoteDataManager.minimumPlayersNeededToVote){
            ctx?.source?.player?.sendMessage(TextUtil.getVoteNotEnoughPlayersText())
            return 1
        }

        if(HulisFabricServerVote.serverVoteThread.isRunning){
            sendVoteRunningMessage(ctx)
            return 1
        }
        val goal = WeatherVoteGoals.valueOf(StringArgumentType.getString(ctx,"votegoal"))
        if (goal in WeatherVoteGoals.entries){
            ctx?.source?.server?.sendMessage(TextUtil.getVoteStartedText(ctx, null, null,null, goal, VoteTypes.WEATHER))
            executeWeather(goal)
            return 1
        }
        return Int.MIN_VALUE
    }

    private fun executeWeather(goal: WeatherVoteGoals) {
        HulisFabricServerVote.serverVoteThread.voteType = VoteTypes.WEATHER
        HulisFabricServerVote.serverVoteThread.weathervoteGoal = goal
        HulisFabricServerVote.serverVoteThread.startVote()
    }

    private fun executeSkipVote(ctx: CommandContext<ServerCommandSource>?): Int {
        if(HulisFabricServerVote.SERVER.playerManager.playerList.size < HulisFabricServerVote.config.hulisFabricServerVoteDataManager.minimumPlayersNeededToVote){
            ctx?.source?.player?.sendMessage(TextUtil.getVoteNotEnoughPlayersText())
            return 1
        }
        if(HulisFabricServerVote.serverVoteThread.isRunning){
            sendVoteRunningMessage(ctx)
            return 1
        }
        val goal = SkipVoteGoals.valueOf(StringArgumentType.getString(ctx,"votegoal"))
        if (goal in SkipVoteGoals.entries){
            ctx?.source?.server?.sendMessage(TextUtil.getVoteStartedText(ctx, goal, null,null, null, VoteTypes.SKIP))
            executeSkip(goal)
            return 1
        }
        return Int.MIN_VALUE
    }

    private fun executeSkip(goal: SkipVoteGoals){
        HulisFabricServerVote.serverVoteThread.voteType = VoteTypes.SKIP
        HulisFabricServerVote.serverVoteThread.skipVoteGoal = goal
        HulisFabricServerVote.serverVoteThread.startVote()
    }


    private fun voteNo(ctx: CommandContext<ServerCommandSource>?): Int {
        val player = ctx?.source?.player
        if(checkVote(ctx)){
            ctx?.source?.player?.sendMessage(TextUtil.getVoteNoText())
            HulisFabricServerVote.INSTANCE.serverVoteThread.voteNo(player!!.uuid)
        }
        return Int.MIN_VALUE
    }


    private fun voteYes(ctx: CommandContext<ServerCommandSource>?): Int {
        val player = ctx?.source?.player
        if(checkVote(ctx)){
            ctx?.source?.player?.sendMessage(TextUtil.getVoteYesText())
            HulisFabricServerVote.INSTANCE.serverVoteThread.voteYes(player!!.uuid)
        }
        return Int.MIN_VALUE
    }

    private fun checkVote(ctx: CommandContext<ServerCommandSource>?) : Boolean {
        val player = ctx?.source?.player
        if(HulisFabricServerVote.INSTANCE.serverVoteThread.hasVoted(player?.uuid)){
            // If the player has already voted, don't let them vote again
            ctx?.source?.player?.sendMessage(TextUtil.getVoteAlreadyVotedText())
            return false
        }
        if(!HulisFabricServerVote.INSTANCE.serverVoteThread.isRunning){
            // If no vote is running, don't let the player vote
            ctx?.source?.player?.sendMessage(TextUtil.getVoteNoRunningText())
            return false
        }
        return true
    }

    private fun sendVoteRunningMessage(ctx: CommandContext<ServerCommandSource>?){
        ctx?.source?.player?.sendMessage(TextUtil.getVoteRunningText())
    }
    private fun isVoteCooldownOver(): Boolean {
        return System.currentTimeMillis() - HulisFabricServerVote.lastVoted > HulisFabricServerVote.config.hulisFabricServerVoteDataManager.voteCooldownInMinutes * 60000
    }
}