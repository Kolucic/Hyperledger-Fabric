package it.unicam.cs.hyperledgerfabricwizard.fabric.ca

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import it.unicam.cs.hyperledgerfabricwizard.fabric.ca.CAServerConfig.Identity
import it.unicam.cs.hyperledgerfabricwizard.utils.toYaml

/**
 * Rappresenta il file di configurazione del fabric-ca-server.
 *
 * Tra le configurazioni troviamo le proprietà che il certificato X509 deve aver,
 * le credenziali per accedere al server del CaAdmin [Identity], nome del ca, ecc.
 */
class CAServerConfig(ca: it.unicam.cs.hyperledgerfabricwizard.client.Ca) {
    class Tls(val enabled: Boolean) {
        //val certfile = "../fabric-ca-client/msp/signcerts/cert.pem"
        //val keyfile = "../fabric-ca-client/msp/keystore"
        /*val clientauth = object : Any() {
            //val type = "noclientcert"
            //val certfiles = null
        }*/
    }

    class Ca(val name: String)
    class Identity(val name: String, @JsonProperty("pass") val password: String) {
        val type = "client"
        val affiliation = ""
        val attrs: Map<String, Any>

        init {
            attrs = mutableMapOf()
            attrs += "hf.Registrar.Roles" to "*"
            attrs += "hf.Revoker" to true
            attrs += "hf.IntermediateCA" to true
            attrs += "hf.Registrar.DelegateRoles" to "*"
            attrs += "hf.GenCRL" to true
            attrs += "hf.Registrar.Attributes" to "*"
            attrs += "hf.AffiliationMgr" to true
        }
    }

    class Registry(val identities: List<Identity>) {
        val maxenrollments = -1
    }

    class DB(val type: String = "sqlite3", val datasource: String = "fabric-ca-server.db")

    @JsonInclude(JsonInclude.Include.NON_NULL)
    class CSR(
            val cn: String? = "fabric-ca-server",
            val names: List<X509> = listOf(X509()),
            val hosts: List<String> = listOf("localhost","example.com")
    ) {
        class X509(
                @JsonProperty("C") val country: String = "IT",
                @JsonProperty("ST") val state: String = "Italy",
                @JsonProperty("L") val location: String = "",
                @JsonProperty("O") val organization: String = "Hyperledger"
        ) {
            @JsonProperty("OU")
            val organizationalUnit = ""
        }

        var ca: Any? = object : Any() {
            val expiry = "131400h"
            val pathlength = 1
        }

    }

    class Signing() {
        val default = object : Any() {
            val usage = listOf("digital signature")
            val expiry = "8760h"
        }
        val profiles = object : Any() {
            /*val ca = object : Any() {
                val usage = listOf("cert sign", "crl sign")
                val expiry = "43800h"
                val caconstraint = object : Any() {
                    val isca = true
                    val maxpathlen = 0
                }
            }*/
            val tls = object : Any() {
                val usage = listOf(
                        "signing",
                        "key encipherment",
                        "server auth",
                        "client auth",
                        "key agreement"
                )
                //val expiry = "8760h"
            }
        }
    }
    class Bccsp
    {
            @JsonProperty("default") val default: String ="SW"
                @JsonProperty("hash") val hash: String ="SHA2"
                @JsonProperty("security") val security: String ="256"
                @JsonProperty("keystore") val keystore: String ="msp/keystore"

    }


    //val port = 7054
    val tls = Tls(true)
    val ca = Ca(ca.name)
    val registry: Registry
    val db = DB()
    val csr: CSR
    val signing = Signing()
    val bccsp =Bccsp()


    init {
        val x509 = if (ca.state == null) CSR.X509(organization = ca.org.toString()) else {
            CSR.X509(country = ca.state.code, state = ca.state.name, organization = ca.org.toString())
        }
        val hosts = mutableListOf("localhost", ca.url)
        this.csr = CSR(
                cn = ca.toString(),
                names = listOf(x509),
                hosts = hosts
        )
        val identity = Identity(ca.username, ca.password)
        this.registry = Registry(listOf(identity))
    }

    override fun toString(): String {
        return this.toYaml()
    }
}
