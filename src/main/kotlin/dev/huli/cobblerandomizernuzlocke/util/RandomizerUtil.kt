package dev.huli.cobblerandomizernuzlocke.util

import com.cobblemon.mod.common.api.pokemon.PokemonSpecies
import com.cobblemon.mod.common.pokemon.Pokemon
import com.cobblemon.mod.common.pokemon.Species

object RandomizerUtil {
    fun getRandomPokemon(oldPokemon: Pokemon): Pokemon {
        val level: Int = oldPokemon.level
        val isShiny:Boolean = oldPokemon.shiny
        val species: Species = getRandomSpecies(oldPokemon)
        val pokemon:Pokemon = PokemonSpecies.random().create(level)
        pokemon.shiny = isShiny
        return pokemon
    }


    private fun getEvolutionStage(species: Species): Int {
        if(species.preEvolution == null) return 1
        if(species.preEvolution != null && species.evolutions.isNotEmpty()) return 2
        if(species.preEvolution != null && species.preEvolution!!.species.preEvolution == null) return 2
        return 3
    }

    fun getRandomSpecies(oldPokemon: Pokemon): Species {
        val evoStage:Int = getEvolutionStage(oldPokemon.species)
        lateinit var newSpecies:Species
        if(!oldPokemon.isLegendary() && !oldPokemon.isMythical() && !oldPokemon.isUltraBeast()){
            do {
                newSpecies = PokemonSpecies.random()

            }while (newSpecies.labels.contains("legendary")
                || newSpecies.labels.contains("mythical")
                || newSpecies.labels.contains("ultra_beast")
                || getEvolutionStage(newSpecies) != evoStage
                || newSpecies.name == "missingno"
            )
        }
        else{
            val legendarySpecies = PokemonSpecies.species.filter { species: Species ->
                (species.labels.contains("legendary")
                    || species.labels.contains("mythical")
                    || species.labels.contains("ultra_beast"))
                        && species.implemented
            }
            newSpecies = PokemonSpecies.getByPokedexNumber(legendarySpecies.random().nationalPokedexNumber)!!
        }
        return newSpecies
    }
    fun getRandomPokemonChaos(oldPokemon: Pokemon): Pokemon {
        val level: Int = oldPokemon.level
        val isShiny:Boolean = oldPokemon.shiny
        val pokemon:Pokemon = PokemonSpecies.random().create(level)
        pokemon.shiny = isShiny
        return pokemon
    }

}