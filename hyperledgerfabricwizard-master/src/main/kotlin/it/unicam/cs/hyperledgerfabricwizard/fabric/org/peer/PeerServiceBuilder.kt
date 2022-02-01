package it.unicam.cs.hyperledgerfabricwizard.fabric.org.peer

import it.unicam.cs.hyperledgerfabricwizard.client.Peer
import it.unicam.cs.hyperledgerfabricwizard.fabric.Globals
import it.unicam.cs.hyperledgerfabricwizard.utils.docker.Service

class PeerServiceBuilder(
        private val peer: Peer,
        private val mspPath: String,
        private val tlsPath: String
) : Service.ServiceBuilder() {
    private val localMspID = peer.alternativeDomain

    override fun build(): Service {
        val url = if (this.peer.isLocalhost()) {
            this.peer.toString()
        } else {
            this.peer.url
        }
        this.image("hyperledger/fabric-peer:${Globals.VERSION}")
        this.workingDir("/opt/gopath/src/github.com/hyperledger/fabric/peer")
        this.command("peer node start")
        this.addPort(this.peer.port, this.peer.port)
        this.containerName("$peer")

        this.addVolume("/var/run/docker.sock", "/var/run/docker.sock")
        this.addVolume(".", "/var/hyperledger/production")
        this.addVolume(this.mspPath, "/etc/hyperledger/fabric/msp")
        this.addVolume(this.tlsPath, "/etc/hyperledger/fabric/tls")

        /* Imposto le variabili d'ambiente per il peer */
        // - CORE_VM_DOCKER_HOSTCONFIG_NETWORKMODE=${COMPOSE_PROJECT_NAME}_test
        this.addEnvironment("CORE_VM_ENDPOINT", "unix:///host/var/run/docker.sock")
        this.addEnvironment("FABRIC_LOGGING_SPEC", "INFO")
        this.addEnvironment("CORE_PEER_TLS_ENABLED", "true")
        this.addEnvironment("CORE_PEER_PROFILE_ENABLED", "true")
        this.addEnvironment("CORE_PEER_TLS_CERT_FILE", "/etc/hyperledger/fabric/tls/server.crt")
        this.addEnvironment("CORE_PEER_TLS_KEY_FILE", "/etc/hyperledger/fabric/tls/server.key")
        this.addEnvironment("CORE_PEER_TLS_ROOTCERT_FILE", "/etc/hyperledger/fabric/tls/ca.crt")
        /* --------------------------------------------------------------------------- */
        this.addEnvironment("CORE_PEER_ID", "$peer")
        this.addEnvironment("CORE_PEER_LOCALMSPID", this.localMspID)

        this.addEnvironment("CORE_PEER_ADDRESS", url + ":" + this.peer.port)
        this.addEnvironment("CORE_PEER_LISTENADDRESS", "0.0.0.0:${this.peer.port}")
        //this.addEnvironment("CORE_PEER_CHAINCODEADDRESS", url + ":" + this.peer.port)
        //this.addEnvironment("CORE_PEER_CHAINCODELISTENADDRESS", "0.0.0.0:${this.peer.port + 1}")
        this.addEnvironment("CORE_PEER_ADDRESSAUTODETECT", "true")
        //this.addEnvironment("CORE_PEER_GOSSIP_BOOTSTRAP", url + ":" + this.peer.port)
        return super.build()
    }
}
