package dev.huli.cobblerandomizernuzlocke.config

import com.google.gson.Gson
import com.google.gson.stream.JsonReader
import dev.huli.cobblerandomizernuzlocke.CobbleRandomizerNuzlocke
import dev.huli.cobblerandomizernuzlocke.managers.CobbleRandomizerDataManager
import dev.huli.cobblerandomizernuzlocke.util.Adapters
import java.io.File
import java.io.FileNotFoundException
import java.io.FileReader
import java.io.FileWriter
import java.io.IOException

class CobbleRandomizerConfig {
    var randomizerDataManager: CobbleRandomizerDataManager = CobbleRandomizerDataManager()

    fun writeConfig(){
        val dir:File = CobbleRandomizerNuzlocke.configDir
        dir.mkdirs()
        val gson = Adapters.PRETTY_MAIN_GSON
        val config = CobbleRandomizerConfig()
        try {
            val file = File(dir,"randomizernuzlocke.json")
            if (file.exists() && file.length()>0) return
            file.createNewFile()
            val writer = FileWriter(file)
            val json:String = gson.toJson(config)
            writer.write(json)
            writer.close()
        }
        catch (e:IOException){
            println(e)
        }
    }

    fun getConfig(): CobbleRandomizerConfig? {
        val dir:File = CobbleRandomizerNuzlocke.configDir
        dir.mkdirs()
        val gson = Adapters.PRETTY_MAIN_GSON
        val file = File(dir,"randomizernuzlocke.json")
        lateinit var reader:JsonReader
        try {
            reader = JsonReader(FileReader(file))
        }
        catch (e : FileNotFoundException){
            println(e)
            return null
        }

        return gson.fromJson(reader,CobbleRandomizerConfig().javaClass)
    }

    fun updateConfig(){
        val config : CobbleRandomizerConfig = this
        val dir:File = CobbleRandomizerNuzlocke.configDir
        dir.mkdirs()
        val gson = Adapters.PRETTY_MAIN_GSON
        try {
            val file = File(dir,"randomizernuzlocke.json")
            file.createNewFile()
            val writer = FileWriter(file)
            val json:String = gson.toJson(config)
            writer.write(json)
            writer.close()
        }
        catch (e:IOException){
            println(e)
        }
    }
}