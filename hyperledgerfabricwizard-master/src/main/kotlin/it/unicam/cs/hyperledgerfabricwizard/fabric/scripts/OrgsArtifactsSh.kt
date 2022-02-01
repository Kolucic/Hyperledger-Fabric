package it.unicam.cs.hyperledgerfabricwizard.fabric.scripts

import it.unicam.cs.hyperledgerfabricwizard.utils.Sh
import it.unicam.cs.hyperledgerfabricwizard.utils.appendNewline

/**
 * Script che esegue gli script [EntitiesArtifactsSh] per ogni org.
 */
class OrgsArtifactsSh(entitiesArtifactsShPaths: List<String>) : Sh() {
    private val output: String = buildString {
        entitiesArtifactsShPaths.forEach {
            appendNewline("bash $it")
        }
    }

    override fun toString(): String {
        return "${super.toString()}$output"
    }
}