package it.unicam.cs.hyperledgerfabricwizard.client

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import it.unicam.cs.hyperledgerfabricwizard.fabric.folder.GenesisFolder
import it.unicam.cs.hyperledgerfabricwizard.fabric.folder.MspFolder
import it.unicam.cs.hyperledgerfabricwizard.fabric.folder.TlsFolder

/**
 * Rappresenta un membro di una organizzazione in una rete HyperledgerFabric.
 *
 * Il membro è identificato da un [name], [domain], [password].
 *
 * La specifica del membro è definita con le seguenti sottoclassi: [Peer], [Orderer], [Client]
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonDeserialize(using = Entity.EntityDeserializer::class)
sealed class Entity(
        val name: String,
        val domain: String,
        val state: State? = null
) {
    class EntityDeserializer : StdDeserializer<Entity>(Entity::class.java) {
        override fun deserialize(p: JsonParser, ctxt: DeserializationContext): Entity {
            val mapper = p.codec as ObjectMapper
            val node: JsonNode = mapper.readTree(p)
            if (node.has("isAnchor"))
                return mapper.treeToValue(node, Peer::class.java)
            if (node.has("isAdmin"))
                return mapper.treeToValue(node, Client::class.java)
            return mapper.treeToValue(node, Orderer::class.java)
        }
    }

    @JsonIgnore
    val alternativeDomain = this.domain.replace(".", "-")


    override fun toString(): String {
        return "$name.$domain"
    }
}

@JsonDeserialize
class Client(
        name: String,
        domain: String,
        val isAdmin: Boolean = false,
        state: State? = null
) : Entity(name, domain, state) {
    @JsonIgnore
    lateinit var mspFolder: MspFolder
}

@JsonDeserialize
class Peer(
        name: String,
        domain: String,
        val isAnchor: Boolean = false,
        override val url: String,
        override val port: Int,
        state: State? = null
) : Entity(name, domain, state), Address{
    @JsonIgnore
    lateinit var tlsFolder: TlsFolder
}

@JsonDeserialize
class Orderer(
        name: String,
        domain: String,
        override val url: String,
        override val port: Int,
        state: State? = null
) : Entity(name, domain, state), Address {
    @JsonIgnore
    lateinit var tlsFolder: TlsFolder
    @JsonIgnore
    lateinit var genesisFolder: GenesisFolder
}

val Entity.type: String
    get() {
        return when (this) {
            is Peer -> "peer"
            is Orderer -> "orderer"
            is Client -> if (this.isAdmin) "admin" else "client"
        }
    }