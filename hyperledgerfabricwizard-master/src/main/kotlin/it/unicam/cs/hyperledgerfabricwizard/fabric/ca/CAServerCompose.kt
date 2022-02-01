package it.unicam.cs.hyperledgerfabricwizard.fabric.ca

import it.unicam.cs.hyperledgerfabricwizard.client.Ca
import it.unicam.cs.hyperledgerfabricwizard.utils.docker.Compose
import it.unicam.cs.hyperledgerfabricwizard.utils.docker.Service
import it.unicam.cs.hyperledgerfabricwizard.utils.toYaml

/**
 * Crea un file docker-compose.yaml per istanziare un fabric-ca-server
 */
class CAServerCompose(ca: Ca, localPath: String) {
    private val compose: Compose

    init {
        val composeBuilder = Compose.builder()
        val service = CAServiceBuilder(ca, localPath).build()
        this.compose = composeBuilder.apply {
            version(2)
            addService(service.containerName!!, service)
        }.build()
    }

    override fun toString(): String {
        return this.compose.toYaml()
    }
}