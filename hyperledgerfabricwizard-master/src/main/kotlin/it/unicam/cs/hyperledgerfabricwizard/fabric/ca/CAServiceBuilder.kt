package it.unicam.cs.hyperledgerfabricwizard.fabric.ca

import it.unicam.cs.hyperledgerfabricwizard.client.Ca
import it.unicam.cs.hyperledgerfabricwizard.fabric.Globals
import it.unicam.cs.hyperledgerfabricwizard.utils.docker.Service

/**
 * Builder modificato per la creazione di un service per istanziare un fabric-ca-server
 */
class CAServiceBuilder(private val ca: Ca, private val localPath: String) : Service.ServiceBuilder() {
    private val image = "hyperledger/fabric-ca:${Globals.CA_VERSION}"
    private val command = "sh -c 'fabric-ca-server start -b ${ca.username}:${ca.password} -d'"
    private val serverConfigPath = "/etc/hyperledger/fabric-ca-server"
    private val serverPort = 7054

    override fun build(): Service {
        this.image(this.image)
        this.command(this.command)
        //
        this.addEnvironment("FABRIC_CA_HOME", "/etc/hyperledger/fabric-ca-server")
        this.addEnvironment("FABRIC_CA_SERVER_CA_NAME", ca.name)
        this.addEnvironment("FABRIC_CA_SERVER_TLS_ENABLED", "true")
        this.addEnvironment("FABRIC_CA_SERVER_PORT", "${this.ca.port}")
        //
        this.addVolume(localPath, serverConfigPath)
        this.addPort(this.ca.port, this.ca.port)
        this.containerName(this.ca.name)
        return super.build()
    }
}
