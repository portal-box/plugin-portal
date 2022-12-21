package com.zestarr.pluginportal.utils

import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.io.FileReader
import java.io.FileWriter
import java.io.IOException

object JsonUtils {
    private val GSON = GsonBuilder().setPrettyPrinting().create()
    private val MAP_TYPE = object : TypeToken<Map<String?, Any?>?>() {}.type
    @Throws(IOException::class)
    fun readJson(filePath: String?): MutableMap<String, Any> {
        FileReader(filePath).use { reader -> return GSON.fromJson(reader, MAP_TYPE) }
    }

    @Throws(IOException::class)
    fun writeJson(filePath: String?, data: Map<String, Any>?) {
        FileWriter(filePath).use { writer -> GSON.toJson(data, writer) }
    }

    @Throws(IOException::class)
    fun updateJson(filePath: String?, updates: Map<String, Any>?) {
        val data = readJson(filePath)
        data.putAll(updates!!)
        writeJson(filePath, data)
    }
}