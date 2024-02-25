package dev.huli.hulisfabricservervote.managers

class HulisFabricServerVoteDataManager {

    var playerPercentageNeededToVoteYes: Int = 50
    var minimumPlayersNeededToVote: Int = 1
    var voteTimeInSeconds: Int = 60
    var voteCooldownInMinutes: Int = 60

    var prefixText: String = "[ServerVote]"
    var voteCountText: String = "%playersVoted%/%playersMax% have voted yes"
    var voteYesText: String = "You have voted yes"
    var voteNoText: String = "You have voted no"
    var voteAlreadyVotedText: String = "You have already voted"
    var voteNoRunningText: String = "No vote is currently running"
    var voteRunningText: String = "A vote is already running"
    var voteStartedText: String = "A vote has been started to %voteGoal% by %player%"
    var voteTimeoutText: String = "The vote has timed out"
    var votePassedText: String = "The vote has passed"
    var voteFailedText: String = "The vote has failed"
    var voteCooldownText: String = "You must wait %cooldown% minutes before voting again"
    var voteNotEnoughPlayersText: String = "Not enough players online to start a vote"




    fun HulisFabricServerVoteDataManager(){

    }
}