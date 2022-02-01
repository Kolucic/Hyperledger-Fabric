package it.unicam.cs.hyperledgerfabricwizard.fabric.folder

import it.unicam.cs.hyperledgerfabricwizard.client.*
import it.unicam.cs.hyperledgerfabricwizard.fabric.Globals.REGISTER_ENROLL_SH
import it.unicam.cs.hyperledgerfabricwizard.fabric.scripts.RegisterEnrollSh
import it.unicam.cs.hyperledgerfabricwizard.fabric.scripts.registerEnrollEntity
import it.unicam.cs.hyperledgerfabricwizard.utils.Directory
import it.unicam.cs.hyperledgerfabricwizard.utils.Folder
import it.unicam.cs.hyperledgerfabricwizard.utils.ToFile

/**
 * La [EntityFolder] è la cartella che contiene tutti i file relativi ad un membro della org: [Peer], [Orderer], [Client].
 *
 * Per le entità di tipo [Peer] e [Orderer] viene istanziata una cartella leggermente modificata [PeerFolder] che conterrà in più
 * la cartella [TlsFolder] e i file docker-compose.yaml per avviare le entità.
 */
open class EntityFolder constructor(folder: Folder, entity: Entity, ca: Ca, fabricCAClientFolder: FabricCAClientFolder) : Folder(folder, entity.name) {
    companion object {
        fun newInstance(folder: Folder, entity: Entity, ca: Ca, fabricCAClientFolder: FabricCAClientFolder): EntityFolder {
            return when (entity) {
                is Peer -> PeerFolder(folder, entity, ca, fabricCAClientFolder)
                is Orderer -> OrdererFolder(folder, entity, ca, fabricCAClientFolder)
                else -> EntityFolder(folder, entity, ca, fabricCAClientFolder)
            }
        }
    }

    @Directory
    val mspFolder = MspFolder(this, ca)

    @ToFile(REGISTER_ENROLL_SH)
    open val registerEnrollSh: String = RegisterEnrollSh.registerEnrollEntity(entity, ca, this.relativePathTo(fabricCAClientFolder), fabricCAClientFolder.relativePathTo(this.mspFolder))

    init {
        if (entity is Client)
            entity.mspFolder = this.mspFolder
    }
}