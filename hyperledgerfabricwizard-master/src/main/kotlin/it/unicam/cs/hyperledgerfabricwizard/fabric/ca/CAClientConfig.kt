package it.unicam.cs.hyperledgerfabricwizard.fabric.ca

import com.fasterxml.jackson.annotation.JsonProperty
import it.unicam.cs.hyperledgerfabricwizard.client.Ca
import it.unicam.cs.hyperledgerfabricwizard.utils.toYaml
import java.nio.file.Path

/**
 * Rappresenta il file di configurazione di fabric-ca-client
 */
class CAClientConfig(ca: Ca, fabricCaServerPath: String) {
    @JsonProperty("caname")
    val caName = ca.name

    @JsonProperty("mspdir")
    val mspDir = "msp"
    val url = "https://${ca.url}:${ca.port}"
    val csr = (if (ca.state == null) {
        CAServerConfig.CSR(cn = "admin", names = listOf(CAServerConfig.CSR.X509(organization = ca.org.toString())))
    } else {
        CAServerConfig.CSR(cn = "admin", names = listOf(CAServerConfig.CSR.X509(country = ca.state.code, state = ca.state.name, organization = ca.org.toString())))
    }).apply {
        this.ca = null
    }

    class Tls(val certfiles: String)

    val tls = Tls(Path.of(fabricCaServerPath, "tls-cert.pem").toString())

    val id = object : Any() {
        val maxenrollments = 0
    }

    val bccsp = object : Any() {
        val default = "SW"
        val sw = object : Any() {
            val hash = "SHA2"
            val security = "256"
            val filekeystore = object : Any() {
                val keystore = "msp/keystore"
            }
        }
    }

    override fun toString(): String {
        return this.toYaml()
    }
}