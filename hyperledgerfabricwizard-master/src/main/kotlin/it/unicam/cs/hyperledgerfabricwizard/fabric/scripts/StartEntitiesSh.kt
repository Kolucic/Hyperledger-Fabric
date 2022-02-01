package it.unicam.cs.hyperledgerfabricwizard.fabric.scripts

import it.unicam.cs.hyperledgerfabricwizard.utils.Sh
import it.unicam.cs.hyperledgerfabricwizard.utils.appendNewline

/**
 * Script di test per avviare i container delle org in locale.
 */
class StartEntitiesSh(dockerComposePaths: List<String>) : Sh() {
    private val output: String = buildString {
        dockerComposePaths.forEach {
            appendNewline(DockerComposeCli(it).up)
        }
    }

    override fun toString(): String {
        return super.toString() + output
    }
}