package it.unicam.cs.hyperledgerfabricwizard.fabric.scripts

import it.unicam.cs.hyperledgerfabricwizard.client.Channel
import it.unicam.cs.hyperledgerfabricwizard.client.Client
import it.unicam.cs.hyperledgerfabricwizard.client.Network
import it.unicam.cs.hyperledgerfabricwizard.client.Peer
import it.unicam.cs.hyperledgerfabricwizard.fabric.folder.NetworkFolder
import it.unicam.cs.hyperledgerfabricwizard.fabric.folder.TlsFolder
import it.unicam.cs.hyperledgerfabricwizard.utils.Folder
import it.unicam.cs.hyperledgerfabricwizard.utils.Sh
import it.unicam.cs.hyperledgerfabricwizard.utils.appendNewline

/**
 * Classe che crea il codice per aggiungere i peer delle org appartenenti al canale allo stesso.
 *
 * Per ogni org facente parte del consorzio amministratore del canale viene scelto un admin che
 * aggiungerà ogni peer al canale.
 */
class JoinChannelSh(folder: Folder, network: Network, channel: Channel, networkFolder: NetworkFolder) : Sh() {
    private val output: String

    init {
        val orgs = channel.consortium.orgs
        this.output = buildString {
            orgs.forEach { org ->
                val admin: Client = (org.entities.find {
                    it is Client && it.isAdmin
                } as Client?)!!
                org.entities.filterIsInstance<Peer>().forEachIndexed { i, it ->
                    if (i == 0)
                        appendNewline("#### Joining peers ${org.fullName}")
                    appendNewline("export CORE_PEER_TLS_ENABLED=true")
                    appendNewline("export FABRIC_CFG_PATH=\$PWD/${folder.relativePathTo(networkFolder)}")
                    appendNewline("export CORE_PEER_LOCALMSPID=\"${org.alternativeName}\"")
                    appendNewline("export CORE_PEER_MSPCONFIGPATH=\"\$PWD/${folder.relativePathTo(admin.mspFolder)}\"")
                    appendNewline("export CORE_PEER_TLS_ROOTCERT_FILE=\"\$PWD/${folder.fileRelativePath(it.tlsFolder, TlsFolder.caCrt)}\"")
                    appendNewline("export CORE_PEER_ADDRESS=${it.fullAddress()}")
                    appendNewline("peer channel join -b ./${channel.name}.block")
                    appendNewline("# ---------------------------------------------")
                }
            }
        }
    }

    override fun toString(): String {
        return "${super.toString()}$output"
    }
}