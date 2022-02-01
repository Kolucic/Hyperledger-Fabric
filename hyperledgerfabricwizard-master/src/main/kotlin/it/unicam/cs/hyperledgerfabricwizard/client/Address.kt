package it.unicam.cs.hyperledgerfabricwizard.client

import com.fasterxml.jackson.annotation.JsonIgnore


interface Address {
    val url: String
    val port: Int

    @JsonIgnore
    fun isLocalhost(): Boolean {
        return url == "localhost"
    }

    fun fullAddress(): String {
        return "$url:$port"
    }
}