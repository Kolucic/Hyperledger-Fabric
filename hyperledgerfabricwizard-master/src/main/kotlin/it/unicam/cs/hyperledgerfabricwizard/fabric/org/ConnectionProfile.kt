package it.unicam.cs.hyperledgerfabricwizard.fabric.org

import com.fasterxml.jackson.annotation.JsonProperty
import it.unicam.cs.hyperledgerfabricwizard.client.Network
import it.unicam.cs.hyperledgerfabricwizard.client.Org
import it.unicam.cs.hyperledgerfabricwizard.client.Peer
import it.unicam.cs.hyperledgerfabricwizard.utils.toJson

/**
 * Crea un file che consentirà ai client di connettersi alla rete HyperledgerFabric.
 */
class ConnectionProfile(
        network: Network,
        private val org: Org
) {
    val name: String = network.name + "-${org.name}"
    val version = "1.0.0"
    val client = object : Any() {
        val organization = org.name
        val connection = object : Any() {
            val timeout = object : Any() {
                val peer = object : Any() {
                    val endorser = "300"
                }
            }
        }
    }
    val organizations: Map<String, Any> = mapOf(org.name to object : Any() {
        val mspid = org.toString()
        val peers = org.entities.filterIsInstance<Peer>().map { it.toString() }
        val certificateAuthorities = listOf(org.ca.toString())
    })
    val peers: MutableMap<String, Any> = mutableMapOf()
    val certificateAuthorities: MutableMap<String, Any> = mutableMapOf()

    init {
        org.entities.filterIsInstance<Peer>().forEach {
            this.peers += it.toString() to object : Any() {
                val url = "grpcs://${it.url}:${it.port}"
                val tlsCACerts = mapOf("pem" to "$" + "{CAPEM}")
                val grpcOptions = object : Any() {
                    @JsonProperty("ssl-target-name-override")
                    val ssl = it.toString()
                    val hostnameOverride = it.toString()
                }
            }
        }
        certificateAuthorities += org.ca.toString() to object : Any() {
            val url = "https://" + org.ca.url + ":${org.ca.port}"
            val caName = org.ca.name
            val tlsCACerts = mapOf("pem" to "$" + "{CAPEM}")
            val httpOptions = object : Any() {
                val verify = false
            }
        }
    }

    override fun toString(): String {
        return this.toJson()
    }
}