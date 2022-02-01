package it.unicam.cs.hyperledgerfabricwizard.fabric.scripts

import it.unicam.cs.hyperledgerfabricwizard.utils.Sh
import it.unicam.cs.hyperledgerfabricwizard.utils.appendNewline

/**
 * Classe che avvia il fabric-ca-server di un organizzazione (supponendo che il CA sia locale)
 * ed esegue il registerEnroll.sh per ogni entità dell'org.
 */
class EntitiesArtifactsSh(
        fabricCaServerComposePath: String,
        caAdminRegisterEnrollPath: String,
        connectionProfilePath: String,
        entitiesRegisterEnrollPaths: List<String>
) : Sh() {
    private val output: String

    init {
        val dockerComposeCli = DockerComposeCli(fabricCaServerComposePath)
        output = buildString {
            appendNewline(dockerComposeCli.up)
            appendNewline("sleep 2")
            appendNewline("bash $caAdminRegisterEnrollPath")
            entitiesRegisterEnrollPaths.forEach {
                appendNewline("bash $it")
            }
            appendNewline(dockerComposeCli.down)
            appendNewline("bash $connectionProfilePath")
        }
    }


    override fun toString(): String {
        return "${super.toString()}$output"
    }
}