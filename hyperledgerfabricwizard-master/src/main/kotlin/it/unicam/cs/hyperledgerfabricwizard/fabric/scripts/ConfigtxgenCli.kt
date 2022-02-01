package it.unicam.cs.hyperledgerfabricwizard.fabric.scripts

/**
 * Classe che crea i comandi per configtxgen
 */
class ConfigtxgenCli(configtxPath: String? = null) {
    private val command = "configtxgen" + if (configtxPath == null) "" else " -configPath $configtxPath"

    fun genesisBlock(profileName: String, channelName: String, outputPath: String): String {
        return "$command -profile $profileName -channelID $channelName -outputBlock $outputPath"
    }

    fun channelTx(profileName: String, channelName: String, outputPath: String): String {
        return "$command -profile $profileName -outputCreateChannelTx $outputPath -channelID $channelName"
    }

    fun anchorPeerTx(profileName: String, channelName: String, orgName: String, outputPath: String): String {
        return "$command -profile $profileName -outputAnchorPeersUpdate $outputPath -channelID $channelName -asOrg $orgName"
    }
}