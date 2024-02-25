package dev.huli.hulisfabricservervote.fabric

import dev.huli.hulisfabricservervote.HulisFabricServerVote
import net.fabricmc.api.ModInitializer

class CobblemonFabric : ModInitializer{
    override fun onInitialize() {
        println("Loaded Randomizer")
        HulisFabricServerVote.initialize()
    }
}