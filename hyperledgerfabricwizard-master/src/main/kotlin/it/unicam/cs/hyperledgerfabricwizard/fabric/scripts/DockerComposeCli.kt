package it.unicam.cs.hyperledgerfabricwizard.fabric.scripts

/**
 * Classe che genera i comandi per docker command line
 */
class DockerComposeCli(
        composeFilePath:String? = null
){
    private val command = "docker-compose"+ if(composeFilePath == null) "" else " -f $composeFilePath"

    val up: String
        get() {
            return "$command up -d"
        }

    val down: String
        get() {
            return "$command down"
        }
}