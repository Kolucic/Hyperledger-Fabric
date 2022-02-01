package it.unicam.cs.hyperledgerfabricwizard.fabric.scripts

/**
 * Classe che genera i comandi per il tool fabric-ca-client.
 */
class FabricCAClientCli(folderPath: String) {
    val FABRIC_CA_HOME = folderPath
    private val command = "fabric-ca-client"
    val register = "$command register"
    val enroll = "$command enroll"
}