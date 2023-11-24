package dev.huli.cobblerandomizernuzlocke.fabric

import dev.huli.cobblerandomizernuzlocke.CobbleRandomizerNuzlocke
import net.fabricmc.api.ModInitializer

class CobblemonFabric : ModInitializer{
    override fun onInitialize() {
        println("Loaded Randomizer")
        CobbleRandomizerNuzlocke.initialize()
    }
}