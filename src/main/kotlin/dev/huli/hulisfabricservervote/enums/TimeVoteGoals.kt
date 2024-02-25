package dev.huli.hulisfabricservervote.enums

enum class TimeVoteGoals(val weatherGoal: Int){
    DAY(0),
    NIGHT(13000),
    SUNSET(12000),
    SUNRISE(23000),
    NOON(6000),
    MIDNIGHT(18000);
}
