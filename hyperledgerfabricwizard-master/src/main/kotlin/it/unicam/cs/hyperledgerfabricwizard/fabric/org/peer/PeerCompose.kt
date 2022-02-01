package it.unicam.cs.hyperledgerfabricwizard.fabric.org.peer

import it.unicam.cs.hyperledgerfabricwizard.client.Peer
import it.unicam.cs.hyperledgerfabricwizard.utils.docker.Compose
import it.unicam.cs.hyperledgerfabricwizard.utils.docker.Service
import it.unicam.cs.hyperledgerfabricwizard.utils.toYaml

class PeerCompose(private val peer: Peer, private val mspPath: String, private val tlsPath: String) {
    private val compose: Compose

    init {
        val composeBuilder = Compose.builder()
        val service = PeerServiceBuilder(peer, mspPath, tlsPath).build()
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