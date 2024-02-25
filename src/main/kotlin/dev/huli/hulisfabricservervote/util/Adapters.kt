package dev.huli.hulisfabricservervote.util

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dev.huli.hulisfabricservervote.adapters.HulisFabricServerVoteAdapter
import dev.huli.hulisfabricservervote.config.HulisFabricServerVoteConfig
import java.lang.reflect.Modifier

object Adapters {
    public final val PRETTY_MAIN_GSON: Gson = GsonBuilder()
        .setPrettyPrinting()
        .disableHtmlEscaping()
        .excludeFieldsWithModifiers(Modifier.TRANSIENT)
        .registerTypeAdapter(HulisFabricServerVoteConfig().javaClass,HulisFabricServerVoteAdapter())
        .create()
}