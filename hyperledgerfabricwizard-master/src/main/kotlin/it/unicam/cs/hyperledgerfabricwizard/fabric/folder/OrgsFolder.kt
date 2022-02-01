package it.unicam.cs.hyperledgerfabricwizard.fabric.folder

import it.unicam.cs.hyperledgerfabricwizard.client.Network
import it.unicam.cs.hyperledgerfabricwizard.fabric.Globals
import it.unicam.cs.hyperledgerfabricwizard.fabric.configtx.Configtx
import it.unicam.cs.hyperledgerfabricwizard.fabric.configtx.ConfigtxBuilder
import it.unicam.cs.hyperledgerfabricwizard.fabric.scripts.ConfigtxSh
import it.unicam.cs.hyperledgerfabricwizard.fabric.scripts.OrgsArtifactsSh
import it.unicam.cs.hyperledgerfabricwizard.fabric.scripts.StartEntitiesSh
import it.unicam.cs.hyperledgerfabricwizard.utils.Directories
import it.unicam.cs.hyperledgerfabricwizard.utils.Folder
import it.unicam.cs.hyperledgerfabricwizard.utils.ToFile

/**
 * La cartella /orgs contiene il file configtx.yaml e le sottocartelle [OrgFolder].
 */
class OrgsFolder(folder: Folder, network: Network) : Folder(folder, "orgs") {
    @Directories
    val orgFolders: List<OrgFolder> = this.createOrgFolders(network)

    @ToFile(Globals.CONFIGTX)
    val configtx: Configtx = this.createConfigtx(network)

    @ToFile(Globals.CONFIGXTX_SH)
    val configtxSh = generateConfigtxArtifacts(network)

    @ToFile(Globals.GENERATE_ORGS_ARTIFACTS_SH)
    val orgsArtifactsSh = createOrgsArtifactsSh()

    /*@ToFile("startEntities.sh")
    val startEntitiesSh = this.startEntitiesSh()*/

    private fun startEntitiesSh(): StartEntitiesSh {
        return StartEntitiesSh(this.orgFolders.flatMap { orgFolder ->
            orgFolder.entitiesFolder.entityFolders.filter { it is OrdererFolder || it is PeerFolder }
        }.map {
            fileRelativePath(it, Globals.DOCKER_COMPOSE)
        })
    }

    @Directories
    val channelFolders = createChannelFolders(network, folder as NetworkFolder)

    private fun createOrgsArtifactsSh(): OrgsArtifactsSh {
        return OrgsArtifactsSh(this.orgFolders.map {
            fileRelativePath(it, Globals.GENERATE_ENTITIES_ARTIFACTS_SH)
        })
    }

    private fun createOrgFolders(network: Network): List<OrgFolder> {
        return network.orgs.map {
            OrgFolder(this, it, network)
        }
    }

    private fun createChannelFolders(network: Network, networkFolder: NetworkFolder): List<ChannelFolder> {
        return network.channels.map {
            ChannelFolder(this, network, it, networkFolder)
        }
    }

    private fun createConfigtx(network: Network): Configtx {
        return ConfigtxBuilder(network, this).build()
    }

    private fun generateConfigtxArtifacts(network: Network): ConfigtxSh {
        return ConfigtxSh(network, this.relativePathTo(this), this)
    }
}