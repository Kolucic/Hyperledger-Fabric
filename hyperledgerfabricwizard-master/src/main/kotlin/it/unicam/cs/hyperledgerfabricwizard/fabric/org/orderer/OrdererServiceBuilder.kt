package it.unicam.cs.hyperledgerfabricwizard.fabric.org.orderer

import it.unicam.cs.hyperledgerfabricwizard.client.Orderer
import it.unicam.cs.hyperledgerfabricwizard.fabric.Globals
import it.unicam.cs.hyperledgerfabricwizard.utils.docker.Service

class OrdererServiceBuilder(
        private val orderer: Orderer,
        private val genesisBlockPath: String,
        private val mspPath: String,
        private val tlsPath: String
) : Service.ServiceBuilder() {
    private val ordererPort = 7050

    override fun build(): Service {
        this.image("hyperledger/fabric-orderer:${Globals.VERSION}")
        this.command("orderer")
        this.workingDir("/opt/gopath/src/github.com/hyperledger/fabric")
        this.addPort(this.orderer.port, this.orderer.port)
        this.containerName("$orderer")

        this.addEnvironment("FABRIC_LOGGING_SPEC", "INFO")
        this.addEnvironment("ORDERER_GENERAL_LISTENADDRESS", "0.0.0.0")
        this.addEnvironment("ORDERER_GENERAL_LISTENPORT", "${this.orderer.port}")
        this.addEnvironment("ORDERER_GENERAL_GENESISMETHOD", "file")
        this.addEnvironment("ORDERER_GENERAL_LOCALMSPID", this.orderer.alternativeDomain)
        this.addEnvironment("ORDERER_GENERAL_GENESISFILE", "/var/hyperledger/orderer/orderer.genesis.block")
        this.addEnvironment("ORDERER_GENERAL_LOCALMSPDIR", "/var/hyperledger/orderer/msp")
        this.addEnvironment("ORDERER_GENERAL_TLS_ENABLED", true.toString())
        this.addEnvironment("ORDERER_GENERAL_TLS_PRIVATEKEY", "/var/hyperledger/orderer/tls/server.key")
        this.addEnvironment("ORDERER_GENERAL_TLS_CERTIFICATE", "/var/hyperledger/orderer/tls/server.crt")
        this.addEnvironment("ORDERER_GENERAL_TLS_ROOTCAS", "[/var/hyperledger/orderer/tls/ca.crt]")
        this.addEnvironment("ORDERER_KAFKA_TOPIC_REPLICATIONFACTOR", "1")
        this.addEnvironment("ORDERER_KAFKA_VERBOSE", "true")
        this.addEnvironment("ORDERER_GENERAL_CLUSTER_CLIENTCERTIFICATE", "/var/hyperledger/orderer/tls/server.crt")
        this.addEnvironment("ORDERER_GENERAL_CLUSTER_CLIENTPRIVATEKEY", "/var/hyperledger/orderer/tls/server.key")
        this.addEnvironment("ORDERER_GENERAL_CLUSTER_ROOTCAS", "[/var/hyperledger/orderer/tls/ca.crt]")
        this.addVolume("../../. ${genesisBlockPath}", "/var/hyperledger/orderer/orderer.genesis.block")
        this.addVolume(this.mspPath, "/var/hyperledger/orderer/msp")
        this.addVolume(this.tlsPath, "/var/hyperledger/orderer/tls")
        this.addVolume(".", "/var/hyperledger/production/orderer")
        return super.build()
    }
}
