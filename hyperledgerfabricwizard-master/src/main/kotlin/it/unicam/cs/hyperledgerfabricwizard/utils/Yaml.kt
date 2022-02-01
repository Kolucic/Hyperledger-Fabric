package it.unicam.cs.hyperledgerfabricwizard.utils

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule

fun Any.toYaml(): String {
    return try {
        ObjectMapper(YAMLFactory().disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER))
                .registerKotlinModule()
                .writeValueAsString(this)
    } catch (e: Exception) {
        ""
    }
}


inline fun <reified T> String.fromYaml(): T? {
    return try {
        ObjectMapper(YAMLFactory().disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER))
                .registerKotlinModule()
                    .readValue<T>(this)
    } catch (e: Exception) {
        null
    }
}