package it.unicam.cs.hyperledgerfabricwizard.fabric.folder

import it.unicam.cs.hyperledgerfabricwizard.client.Ca
import it.unicam.cs.hyperledgerfabricwizard.fabric.Globals.DOCKER_COMPOSE
import it.unicam.cs.hyperledgerfabricwizard.fabric.Globals.FABRIC_CA_SERVER_CONFIG
import it.unicam.cs.hyperledgerfabricwizard.fabric.ca.CAServerCompose
import it.unicam.cs.hyperledgerfabricwizard.fabric.ca.CAServerConfig
import it.unicam.cs.hyperledgerfabricwizard.utils.Folder
import it.unicam.cs.hyperledgerfabricwizard.utils.ToFile

class FabricCAServerFolder(folder: Folder, ca: Ca) : Folder(folder, "fabric-ca-server") {
    @ToFile(FABRIC_CA_SERVER_CONFIG)
    val fabricCAServerConfig = CAServerConfig(ca)

    @ToFile(DOCKER_COMPOSE)
    val fabricCAServerCompose = CAServerCompose(ca, this.relativePathTo(this))
}
