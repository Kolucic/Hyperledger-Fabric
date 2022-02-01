package it.unicam.cs.hyperledgerfabricwizard.fabric.folder

import it.unicam.cs.hyperledgerfabricwizard.client.Ca
import it.unicam.cs.hyperledgerfabricwizard.client.Orderer
import it.unicam.cs.hyperledgerfabricwizard.fabric.Globals
import it.unicam.cs.hyperledgerfabricwizard.fabric.org.orderer.OrdererCompose
import it.unicam.cs.hyperledgerfabricwizard.fabric.scripts.RegisterEnrollSh
import it.unicam.cs.hyperledgerfabricwizard.fabric.scripts.registerEnrollEntity
import it.unicam.cs.hyperledgerfabricwizard.utils.Directory
import it.unicam.cs.hyperledgerfabricwizard.utils.Folder
import it.unicam.cs.hyperledgerfabricwizard.utils.ToFile
import java.nio.file.Path

class OrdererFolder(folder: Folder, entity: Orderer, ca: Ca, fabricCAClientFolder: FabricCAClientFolder) : EntityFolder(folder, entity, ca, fabricCAClientFolder) {
    @Directory
    val tlsFolder = TlsFolder(this)

    @ToFile(Globals.REGISTER_ENROLL_SH)
    override val registerEnrollSh: String = RegisterEnrollSh.registerEnrollEntity(entity, ca, this.relativePathTo(fabricCAClientFolder), fabricCAClientFolder.relativePathTo(this.mspFolder), fabricCAClientFolder.relativePathTo(this.tlsFolder), this.relativePathTo(tlsFolder))

    @Directory
    val genesisFolder = GenesisFolder(this)

    @ToFile(Globals.DOCKER_COMPOSE)
    val dockerCompose = OrdererCompose(entity, Path.of(this.relativePathTo(this.genesisFolder), Globals.GENESIS_BLOCK).toString(), this.relativePathTo(this.mspFolder), this.relativePathTo(this.tlsFolder))

    init {
        entity.tlsFolder = this.tlsFolder
        entity.genesisFolder = this.genesisFolder
    }
}