package dev.huli.hulisfabricservervote.config

import com.google.gson.stream.JsonReader
import dev.huli.hulisfabricservervote.HulisFabricServerVote
import dev.huli.hulisfabricservervote.managers.HulisFabricServerVoteDataManager
import dev.huli.hulisfabricservervote.util.Adapters
import java.io.File
import java.io.FileNotFoundException
import java.io.FileReader
import java.io.FileWriter
import java.io.IOException

class HulisFabricServerVoteConfig {
    var hulisFabricServerVoteDataManager: HulisFabricServerVoteDataManager = HulisFabricServerVoteDataManager()

    fun writeConfig(){
        val dir:File = HulisFabricServerVote.configDir
        dir.mkdirs()
        val gson = Adapters.PRETTY_MAIN_GSON
        val config = HulisFabricServerVoteConfig()
        try {
            val file = File(dir,"hfsvconfig.json")
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

    fun getConfig(): HulisFabricServerVoteConfig? {
        val dir:File = HulisFabricServerVote.configDir
        dir.mkdirs()
        val gson = Adapters.PRETTY_MAIN_GSON
        val file = File(dir,"hfsvconfig.json")
        lateinit var reader:JsonReader
        try {
            reader = JsonReader(FileReader(file))
        }
        catch (e : FileNotFoundException){
            println(e)
            return null
        }

        return gson.fromJson(reader,HulisFabricServerVoteConfig().javaClass)
    }

    fun updateConfig(){
        val config : HulisFabricServerVoteConfig = this
        val dir:File = HulisFabricServerVote.configDir
        dir.mkdirs()
        val gson = Adapters.PRETTY_MAIN_GSON
        try {
            val file = File(dir,"hfsvconfig.json")
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