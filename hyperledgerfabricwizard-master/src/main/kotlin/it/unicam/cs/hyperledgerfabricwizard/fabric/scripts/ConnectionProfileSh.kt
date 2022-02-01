package it.unicam.cs.hyperledgerfabricwizard.fabric.scripts

import it.unicam.cs.hyperledgerfabricwizard.client.Org
import it.unicam.cs.hyperledgerfabricwizard.client.Peer
import it.unicam.cs.hyperledgerfabricwizard.utils.Folder
import it.unicam.cs.hyperledgerfabricwizard.utils.Sh
import java.nio.file.Path

/**
 * Crea un file .sh che completa le informazioni mancanti del file [it.unicam.cs.hyperledgerfabricwizard.fabric.org.ConnectionProfile].
 */
class ConnectionProfileSh(
        private val org: Org,
        private val connectionProfileShPath:String,
        private val folder: Folder,
) : Sh() {
    private val oneLinePem = """
        function one_line_pem {
            echo "`awk 'NF {sub(/\\n/, ""); printf "%s\\\\\\\n",${'$'}0;}' ${'$'}1`"
        }
    """.trimIndent()

    private val jsonParser: String

    private val env: Int = org.entities.filterIsInstance<Peer>().count()

    init {
        val a = buildString {
            repeat(env + 1) {
                append("-e \"s#\\\${CAPEM}#\$CP#\" ")
            }
        }

        jsonParser = """
        function json_ccp {
            local CP=${'$'}(one_line_pem ${'$'}1)
            sed $a $connectionProfileShPath
        }
    """.trimIndent()
    }


    override fun toString(): String {
        return super.toString() + buildString {
            append(oneLinePem + "\n")
            append(jsonParser + "\n")
            append("CAPEM=${Path.of(folder.relativePathTo(org.caServerFolder), "ca-cert.pem")}\n")
            append("""
                echo "${'$'}(json_ccp ${'$'}CAPEM)" > $connectionProfileShPath
            """.trimIndent())
        }
    }
}