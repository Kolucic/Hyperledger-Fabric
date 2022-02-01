package it.unicam.cs.hyperledgerfabricwizard.utils

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue

fun Any.toJson(): String {
    return try {
        ObjectMapper().registerModule(KotlinModule(nullIsSameAsDefault = true)).writerWithDefaultPrettyPrinter().writeValueAsString(this)
    } catch (e: Exception) {
        ""
    }
}


inline fun <reified T> String.fromJson(): T? {
    return try {
        ObjectMapper().registerModule(KotlinModule(nullIsSameAsDefault = true)).readValue<T>(this)
    } catch (e: Exception) {
        null
    }
}