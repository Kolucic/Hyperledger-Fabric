package it.unicam.cs.hyperledgerfabricwizard.fabric.scripts

import it.unicam.cs.hyperledgerfabricwizard.client.*
import it.unicam.cs.hyperledgerfabricwizard.fabric.folder.NetworkFolder
import it.unicam.cs.hyperledgerfabricwizard.fabric.folder.TlsFolder
import it.unicam.cs.hyperledgerfabricwizard.utils.Folder
import it.unicam.cs.hyperledgerfabricwizard.utils.Sh
import it.unicam.cs.hyperledgerfabricwizard.utils.appendNewline

/**
 * Classe che crea lo script per la creazione di un canale.
 *
 * Viene scelta prima organizzazione tra quelle che fanno parte del consorzio amministratore.
 * Dell'organizzazione scelta, viene selezionato un admin.
 * Viene infine selezionato un orderer sulla rete per la connesione.
 */
class CreateChannelSh(folder: Folder, network: Network, channel: Channel, networkFolder: NetworkFolder) : Sh() {
    private val output: String

    init {
        val orgs = channel.consortium.orgs
        val org = orgs.first()
        val admin: Client = (org.entities.find {
            it is Client && it.isAdmin
        } as Client?)!!
        val orderer = network.orgs.flatMap { it.entities.filterIsInstance<Orderer>() }.first() as Orderer
        this.output = buildString {
            appendNewline("export CORE_PEER_TLS_ENABLED=true")
            appendNewline("export FABRIC_CFG_PATH=\$PWD/${folder.relativePathTo(networkFolder)}")
            appendNewline("export CORE_PEER_LOCALMSPID=\"${org.alternativeName}\"")
            appendNewline("export CORE_PEER_MSPCONFIGPATH=\"\$PWD/${folder.relativePathTo(admin.mspFolder)}\"")
            appendNewline("peer channel create -o ${orderer.fullAddress()} -c ${channel.name} --ordererTLSHostnameOverride $orderer -f ./${channel.name}.tx --outputBlock ./${channel.name}.block --tls --cafile \$PWD/${folder.fileRelativePath(orderer.tlsFolder, TlsFolder.caCrt)}")
            orgs.forEach {
                if (it.entities.count { entity -> entity is Peer && entity.isAnchor } > 0) {
                    val admin: Client = (it.entities.find {
                        it is Client && it.isAdmin
                    } as Client?)!!
                    appendNewline("#### Adding ${it.fullName} anchor peers definition to channel block")
                    appendNewline("export CORE_PEER_LOCALMSPID=\"${it.alternativeName}\"")
                    appendNewline("export CORE_PEER_MSPCONFIGPATH=\"\$PWD/${folder.relativePathTo(admin.mspFolder)}\"")
                    appendNewline("peer channel update -o ${orderer.fullAddress()} -c ${channel.name} --ordererTLSHostnameOverride $orderer -f ./${it.fullName}-anchor.tx --tls --cafile \$PWD/${folder.fileRelativePath(orderer.tlsFolder, TlsFolder.caCrt)}")
                }
            }
        }
    }


    override fun toString(): String {
        return "${super.toString()}$output"
    }
}