package it.unicam.cs.hyperledgerfabricwizard.fabric.scripts

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonValue
import it.unicam.cs.hyperledgerfabricwizard.client.Ca
import it.unicam.cs.hyperledgerfabricwizard.utils.toYaml

/**
 * La classe crea un file .yaml che abilita i ruoli [Roles.NodeOUs.OUIdentifier].
 */
class Roles(ca: Ca) {
    @JsonProperty("NodeOUs")
    val nodeOUs = NodeOUs(ca)

    class NodeOUs(ca: Ca) {
        class OUIdentifier(@JsonProperty("Certificate") val certificate: String, @JsonProperty("OrganizationalUnitIdentifier") val orgUnitIdentifier: OrgUnitIdentifier) {
            enum class OrgUnitIdentifier {
                CLIENT, PEER, ADMIN, ORDERER;

                @JsonValue
                override fun toString(): String {
                    return super.toString().toLowerCase()
                }
            }
        }

        @JsonProperty("Enable")
        val enabled: Boolean = true

        private val url = ca.url.replace(".", "-")
        private val caName = ca.name.replace(".", "-")
        private val path = "cacerts/$url-${ca.port}-$caName.pem"

        @JsonProperty("ClientOUIdentifier")
        val clientOUIdentifier = OUIdentifier(path, OUIdentifier.OrgUnitIdentifier.CLIENT)

        @JsonProperty("PeerOUIdentifier")
        val peerOUIdentifier = OUIdentifier(path, OUIdentifier.OrgUnitIdentifier.PEER)

        @JsonProperty("AdminOUIdentifier")
        val adminOUIdentifier = OUIdentifier(path, OUIdentifier.OrgUnitIdentifier.ADMIN)

        @JsonProperty("OrdererOUIdentifier")
        val ordererOUIdentifier = OUIdentifier(path, OUIdentifier.OrgUnitIdentifier.ORDERER)
    }

    override fun toString(): String {
        return this.toYaml()
    }
}