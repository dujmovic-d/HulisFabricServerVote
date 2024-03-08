package dev.huli.hulisfabricservervote.obj

import dev.huli.hulisfabricservervote.HulisFabricServerVote
import dev.huli.hulisfabricservervote.enums.SkipVoteGoals
import dev.huli.hulisfabricservervote.enums.VoteTypes
import dev.huli.hulisfabricservervote.enums.WeatherVoteGoals
import dev.huli.hulisfabricservervote.util.TextUtil
import net.minecraft.text.Text
import java.util.UUID
import java.util.concurrent.ConcurrentLinkedQueue

object ServerVoteThread {
    private var thread: Thread? = null // The thread that will run the vote

    @Volatile var isRunning: Boolean = false // Is the vote currently running?
    @Volatile private var voteYes: Int = 0 // How many players have voted yes
    @Volatile private var voteNo: Int = 0 // How many players have voted no

    private var voteTimeout: Int = HulisFabricServerVote.config.hulisFabricServerVoteDataManager.voteTimeInSeconds // How long the vote will last in seconds
    var playersVotedUUIDs: ConcurrentLinkedQueue<UUID> = ConcurrentLinkedQueue() // List of players who have voted

    lateinit var voteType : VoteTypes // Is it a skip vote, weather vote, or time vote?
    lateinit var skipVoteGoal : SkipVoteGoals // What is the goal of the skip vote? (night, day, weather)
    lateinit var weathervoteGoal : WeatherVoteGoals // What is the goal of the weather vote? (clear, rain, thundering)
    var timeVoteGoal : Int = 0 // To what time should the vote skip to?


    fun startVote() {
        if (isRunning) {
            return // If a vote is already running, don't start another one
        }
        isRunning = true
        voteYes = 0
        voteNo = 0
        thread = voteThread()
        thread!!.start() // Start the vote
    }

    private fun broadcastVote() {
        HulisFabricServerVote.INSTANCE.SERVER.playerManager.playerList.forEach { player ->
            player.sendMessage(TextUtil.getvoteCountText(), false)  }
        // Broadcast how many players have voted yes
    }

    fun voteYes(playerUUID: UUID) {
        voteYes++
        playersVotedUUIDs.add(playerUUID)
        broadcastVote()
    }

    fun voteNo(playerUUID: UUID) {
        voteNo++
        playersVotedUUIDs.add(playerUUID)
        broadcastVote()
    }

    private fun reset() {
        // Reset the vote
        isRunning = false
        voteYes = 0
        voteNo = 0
        playersVotedUUIDs.clear()
        HulisFabricServerVote.lastVoted = System.currentTimeMillis()
    }

    fun hasVoted(uuid: UUID?): Boolean {
        // Check if a player has voted
        return playersVotedUUIDs.contains(uuid)
    }

    private fun voteThread() : Thread {
        return Thread {
            broadcastVote()

            repeat(voteTimeout) {
                if (voteYes > voteNo && voteYes > (HulisFabricServerVote.INSTANCE.SERVER.playerManager.playerList.size / 2)) {
                    // If more than half of the players have voted yes, and more players have voted yes than no, pass the vote
                    when (voteType) {
                        VoteTypes.SKIP -> {
                            votePass()
                            when (skipVoteGoal) {
                                SkipVoteGoals.NIGHT -> {
                                    HulisFabricServerVote.INSTANCE.SERVER.worlds.forEach {
                                        it.timeOfDay = 13000 // Set the time to night
                                    }
                                    HulisFabricServerVote.INSTANCE.SERVER.playerManager.playerList.forEach{ player ->
                                        player.sendMessage(TextUtil.getPrefixText("Setting time to $timeVoteGoal"), true)}
                                }

                                SkipVoteGoals.DAY -> {
                                    HulisFabricServerVote.INSTANCE.SERVER.worlds.forEach {
                                        it.timeOfDay = 0 // Set the time to day
                                    }
                                    HulisFabricServerVote.INSTANCE.SERVER.playerManager.playerList.forEach{ player ->
                                        player.sendMessage(TextUtil.getPrefixText("Setting time to $timeVoteGoal"), true)}
                                }

                                SkipVoteGoals.WEATHER -> {
                                    HulisFabricServerVote.INSTANCE.SERVER.worlds.forEach {
                                        it.setWeather(0, 0, false, false) // Set the weather to clear
                                    }
                                    HulisFabricServerVote.INSTANCE.SERVER.playerManager.playerList.forEach{ player ->
                                        player.sendMessage(TextUtil.getPrefixText("Setting weather to $timeVoteGoal"), true)}
                                }
                            }
                        }

                        VoteTypes.WEATHER -> {
                            votePass()

                            HulisFabricServerVote.INSTANCE.SERVER.playerManager.playerList.forEach{ player ->
                                player.sendMessage(TextUtil.getPrefixText("Setting weather to $weathervoteGoal"), true)
                            }
                            when (weathervoteGoal) {
                                WeatherVoteGoals.CLEAR -> {
                                    HulisFabricServerVote.INSTANCE.SERVER.worlds.forEach {
                                        it.setWeather(0, 0, false, false) // Set the weather to clear
                                    }
                                }

                                WeatherVoteGoals.RAIN -> {
                                    HulisFabricServerVote.INSTANCE.SERVER.worlds.forEach {
                                        it.setWeather(0, 0, true, false) // Set the weather to rain
                                    }
                                }

                                WeatherVoteGoals.THUNDERING -> {
                                    HulisFabricServerVote.INSTANCE.SERVER.worlds.forEach {
                                        it.setWeather(0, 0, true, true) // Set the weather to thundering
                                    }
                                }
                            }
                        }

                        VoteTypes.TIME -> {
                            votePass()

                            HulisFabricServerVote.INSTANCE.SERVER.playerManager.playerList.forEach{ player ->
                                player.sendMessage(TextUtil.getPrefixText("Setting time to $timeVoteGoal"), true)
                            }
                            HulisFabricServerVote.INSTANCE.SERVER.worlds.forEach {
                                it.timeOfDay = timeVoteGoal.toLong() // Set the time to the goal
                            }
                        }
                    }

                    reset()
                    return@repeat
                }
                if (voteNo > (HulisFabricServerVote.INSTANCE.SERVER.playerManager.playerList.size / 2)) {
                    // If more than half of the players have voted no, fail the vote
                    voteFail()
                    reset()

                    return@repeat
                }
                Thread.sleep(1000)
            }

            reset()
            isRunning = false
            HulisFabricServerVote.INSTANCE.SERVER.playerManager.playerList.forEach{it.sendMessage(TextUtil.getVoteTimeoutText(),true)}
        }
    }

    private fun votePass() {
        HulisFabricServerVote.INSTANCE.SERVER.playerManager.playerList.forEach{it.sendMessage(TextUtil.getVotePassedText(),true)}
    }
    private fun voteFail() {
        HulisFabricServerVote.INSTANCE.SERVER.playerManager.playerList.forEach{it.sendMessage(TextUtil.getVoteFailedText(),true)}
    }
}