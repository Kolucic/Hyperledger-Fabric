package it.unicam.cs.hyperledgerfabricwizard.fabric.folder

import it.unicam.cs.hyperledgerfabricwizard.client.Network
import it.unicam.cs.hyperledgerfabricwizard.client.Org
import it.unicam.cs.hyperledgerfabricwizard.fabric.Globals.CONNECTION_PROFILE
import it.unicam.cs.hyperledgerfabricwizard.fabric.Globals.CONNECTION_PROFILE_SH
import it.unicam.cs.hyperledgerfabricwizard.fabric.Globals.DOCKER_COMPOSE
import it.unicam.cs.hyperledgerfabricwizard.fabric.Globals.GENERATE_ENTITIES_ARTIFACTS_SH
import it.unicam.cs.hyperledgerfabricwizard.fabric.Globals.REGISTER_ENROLL_SH
import it.unicam.cs.hyperledgerfabricwizard.fabric.org.ConnectionProfile
import it.unicam.cs.hyperledgerfabricwizard.fabric.scripts.ConnectionProfileSh
import it.unicam.cs.hyperledgerfabricwizard.fabric.scripts.EntitiesArtifactsSh
import it.unicam.cs.hyperledgerfabricwizard.fabric.scripts.RegisterEnrollSh
import it.unicam.cs.hyperledgerfabricwizard.fabric.scripts.enrollCaAdmin
import it.unicam.cs.hyperledgerfabricwizard.utils.Directory
import it.unicam.cs.hyperledgerfabricwizard.utils.Folder
import it.unicam.cs.hyperledgerfabricwizard.utils.ToFile

/**
 * La cartella contiene tutti i file di configurazione per l'organizzazione specificata.
 *
 * Prende il nome da [Org.fullName] e contiene le sottocartelle: [FabricCAServerFolder], [FabricCAClientFolder],
 * [EntitiesFolder].
 */
class OrgFolder(folder: Folder, val org: Org, network: Network) : Folder(folder, org.toString()) {
    @Directory
    val fabricCAServerFolder = FabricCAServerFolder(this, org.ca)

    @Directory
    val fabricCAClientFolder = FabricCAClientFolder(this, org, fabricCAServerFolder)

    @Directory
    val entitiesFolder = EntitiesFolder(this, org.entities, org.ca, fabricCAClientFolder)

    @Directory
    val scriptFolder = object : Folder(this, "script") {
        @ToFile(REGISTER_ENROLL_SH)
        val registerEnrollCaAdmin = RegisterEnrollSh.enrollCaAdmin(org.ca, this.relativePathTo(this@OrgFolder.fabricCAClientFolder))

        @ToFile(CONNECTION_PROFILE_SH)
        val connectionProfileSh = ConnectionProfileSh(org, this.fileRelativePath(this@OrgFolder, CONNECTION_PROFILE), this)
    }

    @ToFile(CONNECTION_PROFILE)
    val connectionProfile = ConnectionProfile(network, org)


    @ToFile(GENERATE_ENTITIES_ARTIFACTS_SH)
    val entitiesArtifactsSh: EntitiesArtifactsSh

    init {
        this.org.caServerFolder = this.fabricCAServerFolder
        this.org.caClientFolder = this.fabricCAClientFolder

        this.entitiesArtifactsSh = EntitiesArtifactsSh(
                this.fileRelativePath(this.fabricCAServerFolder, DOCKER_COMPOSE),
                this.fileRelativePath(this.scriptFolder, REGISTER_ENROLL_SH),
                this.fileRelativePath(this.scriptFolder, CONNECTION_PROFILE_SH),
                entitiesFolder.entityFolders.map {
                    this.fileRelativePath(it, REGISTER_ENROLL_SH)
                }
        )
    }
}
