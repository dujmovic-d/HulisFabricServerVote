package dev.huli.cobblerandomizernuzlocke.util

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dev.huli.cobblerandomizernuzlocke.adapters.CobbleRandomizerAdapter
import dev.huli.cobblerandomizernuzlocke.config.CobbleRandomizerConfig
import java.lang.reflect.Modifier

object Adapters {
    public final val PRETTY_MAIN_GSON: Gson = GsonBuilder()
        .setPrettyPrinting()
        .disableHtmlEscaping()
        .excludeFieldsWithModifiers(Modifier.TRANSIENT)
        .registerTypeAdapter(CobbleRandomizerConfig().javaClass,CobbleRandomizerAdapter())
        .create()
}