package dev.huli.cobblerandomizernuzlocke.adapters

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import dev.huli.cobblerandomizernuzlocke.config.CobbleRandomizerConfig
import dev.huli.cobblerandomizernuzlocke.managers.CobbleRandomizerDataManager

class CobbleRandomizerAdapter : TypeAdapter<CobbleRandomizerConfig>() {
    override fun write(p0: JsonWriter?, p1: CobbleRandomizerConfig?)  {
        val gson = Gson()
        val dataManager = p1?.randomizerDataManager
        if (p0 != null) {
            p0.beginObject()
            p0.name("RandomizerEnabled")
            dataManager?.let { p0.value(it.randomizerOn) }
            p0.name("NuzlockeEnabled")
            dataManager?.let { p0.value(it.nuzlockeOn) }
            p0.endObject()
        }
    }

    override fun read(p0: JsonReader?): CobbleRandomizerConfig {
        val config = CobbleRandomizerConfig()
        val dataManager = CobbleRandomizerDataManager()
        if (p0 != null) {
            p0.beginObject()
            while (p0.hasNext()){
                val fieldName = p0.nextName()

                when(fieldName){
                    "RandomizerEnabled"->dataManager.randomizerOn = p0.nextBoolean()
                    "NuzlockeEnabled"->dataManager.nuzlockeOn = p0.nextBoolean()
                    else->{}
                }
            }
            p0.endObject()
        }
    config.randomizerDataManager = dataManager
        return config
    }
}