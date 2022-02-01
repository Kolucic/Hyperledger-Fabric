package it.unicam.cs.hyperledgerfabricwizard.client

import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonInclude

/**
 * Rappresenta un certificate authority di tipo FabricCa in una rete HyperledgerFabric.
 *
 * I campi sotto elencati sono impostazioni modificabili tra cui il certificato che rilascia [X509]
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
class Ca(
        val name: String,
        val username: String = "admin",
        val password: String = "adminpw",
        override val url: String,
        override val port: Int,
        val state: State? = null
) : Address {
    @JsonBackReference
    lateinit var org: Org

    override fun toString(): String {
        return "ca.$org"
    }
}


class State(val name: String, val code: String)