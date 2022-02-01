package it.unicam.cs.hyperledgerfabricwizard.fabric.folder

import it.unicam.cs.hyperledgerfabricwizard.client.Network
import it.unicam.cs.hyperledgerfabricwizard.fabric.Globals.CLIENT_CONFIG_JSON
import it.unicam.cs.hyperledgerfabricwizard.fabric.Globals.CORE_PEER_CONFIG
import it.unicam.cs.hyperledgerfabricwizard.fabric.Globals.ORDERER_CONFIG
import it.unicam.cs.hyperledgerfabricwizard.fabric.Globals.PREREQ_SH
import it.unicam.cs.hyperledgerfabricwizard.fabric.scripts.CorePeer
import it.unicam.cs.hyperledgerfabricwizard.fabric.scripts.OrdererOrder
import it.unicam.cs.hyperledgerfabricwizard.fabric.scripts.PrerequisitesSh
import it.unicam.cs.hyperledgerfabricwizard.utils.*

/**
 * È la cartella root. Prende il nome del [Network.name].
 *
 * Contiene due sotto cartelle /bin e /orgs; la prima conterrà i binari di HyperledgerFabric
 * mentre la seconda conterrà le sotto cartelle [OrgFolder].
 */
class NetworkFolder(parentPath: String, network: Network) : Folder(parentPath, network.name) {
    @Directory
    val binFolder = emptyFolder(this, "bin")

    @Directory
    val orgsFolder = OrgsFolder(this, network)

    @ToFile (ORDERER_CONFIG)
    val ordererOrd= OrdererOrder()

    @ToFile(CORE_PEER_CONFIG)
    val corePeer = CorePeer()

    @ToFile(CLIENT_CONFIG_JSON)
    val clientConfig = network.toJson()

    @ToFile(PREREQ_SH)
    val prerequisitesSh = PrerequisitesSh()
}
