package it.unicam.cs.hyperledgerfabricwizard.fabric.scripts

import it.unicam.cs.hyperledgerfabricwizard.client.Network
import it.unicam.cs.hyperledgerfabricwizard.client.Orderer
import it.unicam.cs.hyperledgerfabricwizard.client.Peer
import it.unicam.cs.hyperledgerfabricwizard.fabric.Globals
import it.unicam.cs.hyperledgerfabricwizard.utils.Folder
import it.unicam.cs.hyperledgerfabricwizard.utils.Sh
import it.unicam.cs.hyperledgerfabricwizard.utils.appendNewline

/**
 * Classe che crea il genesis block, i tx per gli anchor a partire dal configtx.yaml
 */
class ConfigtxSh(network: Network, configtxPath: String, folder: Folder) : Sh() {
    private val configtxgen = ConfigtxgenCli(configtxPath)
    private val output: String
    private val systemGenesis = "./system-genesis-block"
    private val outputPath = "$systemGenesis/${Globals.GENESIS_BLOCK}"

    init {
        val orderers = network.orgs.flatMap { it.entities.filterIsInstance<Orderer>() }
        this.output = buildString {
            appendNewline(configtxgen.genesisBlock(Globals.SYSTEM_CHANNEL, Globals.SYSTEM_CHANNEL_PROFILE, outputPath))
            orderers.forEach {
                appendNewline("cp $outputPath ${folder.relativePathTo(it.genesisFolder.genesisBlockPath)}")
            }
            appendNewline(generateChannelArtifacts(network))
        }
    }

    fun generateChannelArtifacts(network: Network): String {
        var output = ""
        network.channels.forEach { c ->
            output += buildString {
                appendNewline(configtxgen.channelTx(c.name, c.name, "./${c.name}/${c.name}.tx"))
                c.orgs.filter { org ->
                    c.consortium.orgs.contains(org) && org.entities.count { (it is Peer) && it.isAnchor } > 0
                }.forEach { org ->
                    appendNewline(configtxgen.anchorPeerTx(c.name, c.name, org.configtxName, "./${c.name}/${org.fullName}-anchor.tx"))
                }
            }
        }
        return output
    }

    override fun toString(): String {
        return "${super.toString()}$output"
    }
}
