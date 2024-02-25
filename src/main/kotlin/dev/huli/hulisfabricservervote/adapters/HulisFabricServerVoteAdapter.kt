package dev.huli.hulisfabricservervote.adapters

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import dev.huli.hulisfabricservervote.config.HulisFabricServerVoteConfig
import dev.huli.hulisfabricservervote.managers.HulisFabricServerVoteDataManager

class HulisFabricServerVoteAdapter : TypeAdapter<HulisFabricServerVoteConfig>() {
    override fun write(p0: JsonWriter?, p1: HulisFabricServerVoteConfig?)  {
        val gson = Gson()
        val dataManager = p1?.hulisFabricServerVoteDataManager
        if (p0 != null) {
            p0.beginObject()

            p0.name("PercentageYesNeeded")
            dataManager?.let { p0.value(it.playerPercentageNeededToVoteYes) }
            p0.name("MinimumPlayersNeeded")
            dataManager?.let { p0.value(it.minimumPlayersNeededToVote) }
            p0.name("VoteTimeInSeconds")
            dataManager?.let { p0.value(it.voteTimeInSeconds) }
            p0.name("VoteCooldownInMinutes")
            dataManager?.let { p0.value(it.voteCooldownInMinutes) }
            p0.name("PrefixText")
            dataManager?.let { p0.value(it.prefixText) }
            p0.name("VoteCountText")
            dataManager?.let { p0.value(it.voteCountText) }
            p0.name("VoteYesText")
            dataManager?.let { p0.value(it.voteYesText) }
            p0.name("VoteNoText")
            dataManager?.let { p0.value(it.voteNoText) }
            p0.name("VoteAlreadyVotedText")
            dataManager?.let { p0.value(it.voteAlreadyVotedText) }
            p0.name("VoteNoRunningText")
            dataManager?.let { p0.value(it.voteNoRunningText) }
            p0.name("VoteRunningText")
            dataManager?.let { p0.value(it.voteRunningText) }
            p0.name("VoteStartedText")
            dataManager?.let { p0.value(it.voteStartedText) }
            p0.name("VoteTimeoutText")
            dataManager?.let { p0.value(it.voteTimeoutText) }
            p0.name("VotePassedText")
            dataManager?.let { p0.value(it.votePassedText) }
            p0.name("VoteFailedText")
            dataManager?.let { p0.value(it.voteFailedText) }
            p0.name("VoteCooldownText")
            dataManager?.let { p0.value(it.voteCooldownText) }
            p0.name("VoteNotEnoughPlayersText")
            dataManager?.let { p0.value(it.voteNotEnoughPlayersText) }

            p0.endObject()
        }
    }

    override fun read(p0: JsonReader?): HulisFabricServerVoteConfig {
        val config = HulisFabricServerVoteConfig()
        val dataManager = HulisFabricServerVoteDataManager()
        if (p0 != null) {
            p0.beginObject()
            while (p0.hasNext()){
                val fieldName = p0.nextName()

                when(fieldName){
                    "PercentageYesNeeded"->dataManager.playerPercentageNeededToVoteYes = p0.nextInt()
                    "MinimumPlayersNeeded"->dataManager.minimumPlayersNeededToVote = p0.nextInt()
                    "VoteTimeInSeconds"->dataManager.voteTimeInSeconds = p0.nextInt()
                    "VoteCooldownInMinutes"->dataManager.voteCooldownInMinutes = p0.nextInt()
                    "PrefixText"->dataManager.prefixText = p0.nextString()
                    "VoteCountText"->dataManager.voteCountText = p0.nextString()
                    "VoteYesText"->dataManager.voteYesText = p0.nextString()
                    "VoteNoText"->dataManager.voteNoText = p0.nextString()
                    "VoteAlreadyVotedText"->dataManager.voteAlreadyVotedText = p0.nextString()
                    "VoteNoRunningText"->dataManager.voteNoRunningText = p0.nextString()
                    "VoteRunningText"->dataManager.voteRunningText = p0.nextString()
                    "VoteStartedText"->dataManager.voteStartedText = p0.nextString()
                    "VoteTimeoutText"->dataManager.voteTimeoutText = p0.nextString()
                    "VotePassedText"->dataManager.votePassedText = p0.nextString()
                    "VoteFailedText"->dataManager.voteFailedText = p0.nextString()
                    "VoteCooldownText"->dataManager.voteCooldownText = p0.nextString()
                    "VoteNotEnoughPlayersText"->dataManager.voteNotEnoughPlayersText = p0.nextString()
                    else->{}
                }
            }
            p0.endObject()
        }
        config.hulisFabricServerVoteDataManager = dataManager
        return config
    }
}