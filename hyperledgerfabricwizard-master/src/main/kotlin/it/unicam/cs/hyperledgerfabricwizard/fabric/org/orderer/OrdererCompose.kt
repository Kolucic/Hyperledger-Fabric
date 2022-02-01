package it.unicam.cs.hyperledgerfabricwizard.fabric.org.orderer

import it.unicam.cs.hyperledgerfabricwizard.client.Orderer
import it.unicam.cs.hyperledgerfabricwizard.utils.docker.Compose
import it.unicam.cs.hyperledgerfabricwizard.utils.toYaml

class OrdererCompose(orderer: Orderer, genesisBlockPath: String, mspPath: String, tlsPath: String) {
    private val compose: Compose

    init {
        val composeBuilder = Compose.builder()
        val service = OrdererServiceBuilder(orderer, genesisBlockPath, mspPath, tlsPath).build()
        this.compose = composeBuilder.apply {
            version(2)
            addService(service.containerName!!, service)
            addVolume(service.containerName)
        }.build()
    }

    override fun toString(): String {
        return this.compose.toYaml()
    }
}