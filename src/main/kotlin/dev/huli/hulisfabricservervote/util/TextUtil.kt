package dev.huli.hulisfabricservervote.util

import com.mojang.brigadier.context.CommandContext
import dev.huli.hulisfabricservervote.HulisFabricServerVote
import dev.huli.hulisfabricservervote.enums.SkipVoteGoals
import dev.huli.hulisfabricservervote.enums.TimeVoteGoals
import dev.huli.hulisfabricservervote.enums.VoteTypes
import dev.huli.hulisfabricservervote.enums.WeatherVoteGoals
import eu.pb4.placeholders.api.TextParserUtils
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.text.Text

class TextUtil {
    companion object{
    fun getPrefixText(text :String): Text{
        return TextParserUtils.formatText(getPrefix() +" "+ text)
    }
    private fun getPrefix():String{
        return HulisFabricServerVote.config.hulisFabricServerVoteDataManager.prefixText
    }
         fun getvoteCountText():Text{
            var text = HulisFabricServerVote.config.hulisFabricServerVoteDataManager.voteCountText
            text = text.replace("%playersVoted%",HulisFabricServerVote.serverVoteThread.playersVotedUUIDs.size.toString())
            text = text.replace("%playersMax%",HulisFabricServerVote.SERVER.playerManager.currentPlayerCount.toString())
            return getPrefixText(text)
        }
         fun getVoteYesText():Text{
            return getPrefixText(HulisFabricServerVote.config.hulisFabricServerVoteDataManager.voteYesText)
        }
         fun getVoteNoText():Text{
            return getPrefixText(HulisFabricServerVote.config.hulisFabricServerVoteDataManager.voteNoText)
        }
         fun getVoteTimeoutText():Text{
            return getPrefixText(HulisFabricServerVote.config.hulisFabricServerVoteDataManager.voteTimeoutText)
        }
         fun getVotePassedText():Text{
            return getPrefixText(HulisFabricServerVote.config.hulisFabricServerVoteDataManager.votePassedText)
        }
         fun getVoteFailedText():Text{
            return getPrefixText(HulisFabricServerVote.config.hulisFabricServerVoteDataManager.voteFailedText)
        }
        fun getVoteCooldownText():Text{
            var text = HulisFabricServerVote.config.hulisFabricServerVoteDataManager.voteCooldownText
            text = text.replace("%time%",(HulisFabricServerVote.config.hulisFabricServerVoteDataManager.voteCooldownInMinutes - (HulisFabricServerVote.lastVoted*60000)).toString() )
            return getPrefixText(text)
        }
         fun getVoteNotEnoughPlayersText():Text{
            return getPrefixText(HulisFabricServerVote.config.hulisFabricServerVoteDataManager.voteNotEnoughPlayersText)
        }
         fun getVoteRunningText():Text{
            return getPrefixText(HulisFabricServerVote.config.hulisFabricServerVoteDataManager.voteRunningText)
        }
         fun getVoteNoRunningText():Text{
            return getPrefixText(HulisFabricServerVote.config.hulisFabricServerVoteDataManager.voteNoRunningText)
        }
         fun getVoteAlreadyVotedText():Text{
            return getPrefixText(HulisFabricServerVote.config.hulisFabricServerVoteDataManager.voteAlreadyVotedText)
        }
         fun getVoteStartedText(ctx: CommandContext<ServerCommandSource>, skipVoteGoal: SkipVoteGoals?,timeVoteGoal: TimeVoteGoals?,timeInteger: Int?,weatherVoteGoal: WeatherVoteGoals?,voteType: VoteTypes):Text{
            var text = HulisFabricServerVote.config.hulisFabricServerVoteDataManager.voteStartedText

            when(voteType){
                VoteTypes.SKIP -> {
                    if (skipVoteGoal != null) {
                    text = text.replace("%voteGoal%", "skip $skipVoteGoal")
                    }
                }
                VoteTypes.TIME -> {
                    if (timeVoteGoal != null){
                        text = text.replace("%voteGoal%", "set time to $timeVoteGoal")
                    }
                    else if (timeInteger != null){
                        text = text.replace("%voteGoal%", "set time to $timeInteger")
                    }
                }
                VoteTypes.WEATHER -> {
                    if (weatherVoteGoal != null){
                    text = text.replace("%voteGoal%", "set weather to $weatherVoteGoal")
                    }
                }
            }
            text = text.replace("%player%", ctx.source.player?.name.toString())


            return getPrefixText(text)
        }

    }
}