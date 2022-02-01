package it.unicam.cs.hyperledgerfabricwizard.fabric.folder

import it.unicam.cs.hyperledgerfabricwizard.client.Ca
import it.unicam.cs.hyperledgerfabricwizard.client.Peer
import it.unicam.cs.hyperledgerfabricwizard.fabric.Globals
import it.unicam.cs.hyperledgerfabricwizard.fabric.org.peer.PeerCompose
import it.unicam.cs.hyperledgerfabricwizard.fabric.scripts.RegisterEnrollSh
import it.unicam.cs.hyperledgerfabricwizard.fabric.scripts.registerEnrollEntity
import it.unicam.cs.hyperledgerfabricwizard.utils.Directory
import it.unicam.cs.hyperledgerfabricwizard.utils.Folder
import it.unicam.cs.hyperledgerfabricwizard.utils.ToFile

open class PeerFolder(folder: Folder, entity: Peer, ca: Ca, fabricCAClientFolder: FabricCAClientFolder) : EntityFolder(folder, entity, ca, fabricCAClientFolder) {
    @Directory
    val tlsFolder = TlsFolder(this)

    @ToFile(Globals.REGISTER_ENROLL_SH)
    override val registerEnrollSh: String = RegisterEnrollSh.registerEnrollEntity(entity, ca, this.relativePathTo(fabricCAClientFolder), fabricCAClientFolder.relativePathTo(this.mspFolder), fabricCAClientFolder.relativePathTo(this.tlsFolder), this.relativePathTo(tlsFolder))

    @ToFile(Globals.DOCKER_COMPOSE)
    val dockerCompose = PeerCompose(entity, this.relativePathTo(this.mspFolder), this.relativePathTo(this.tlsFolder))

    init {
        entity.tlsFolder = tlsFolder
    }
}