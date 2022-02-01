package it.unicam.cs.hyperledgerfabricwizard.fabric.scripts

import it.unicam.cs.hyperledgerfabricwizard.client.*
import it.unicam.cs.hyperledgerfabricwizard.fabric.ca.CAServerConfig
import it.unicam.cs.hyperledgerfabricwizard.fabric.folder.TlsFolder
import it.unicam.cs.hyperledgerfabricwizard.utils.Builder
import it.unicam.cs.hyperledgerfabricwizard.utils.Sh
import it.unicam.cs.hyperledgerfabricwizard.utils.appendNewline
import java.nio.file.Path


/**
 * Crea un file .sh che permette alle organizzazioni di connettersi al proprio CA e di ricevere i materiali
 * crittografici per le entità.
 */
class RegisterEnrollSh private constructor() : Sh() {

    companion object

    class RegisterEnrollBuilder(fabricCARelativePath: String, private val ca: Ca) : Builder<String> {
        private val fabricCAClient = FabricCAClientCli(fabricCARelativePath)
        private val url = ca.url
        private val port = ca.port

        private var result = ""

        private fun exportHome() = "export FABRIC_CA_HOME=${fabricCAClient.FABRIC_CA_HOME}\n"

        private fun buildPassword(entity: Entity): String {
            return "${entity.name}pw"
        }

        private fun wrap(action: () -> String): String {
            return "set -x\n${action()}\nset +x\n"
        }

        private fun url(username: String, password: String): String {
            return "-u https://$username:$password@$url:$port"
        }

        private fun csr(entity: Entity): String {
            return (if (entity !is Address) "" else "--csr.hosts $entity --csr.hosts ${entity.url}") + if (entity.state != null) " --csr.names C=${entity.state.code},ST='${entity.state.name}'" else ""
        }

        private fun tls(tlsFolderRelativePath: String): String {
            return "-M $tlsFolderRelativePath --enrollment.profile tls"
        }

        private fun msp(mspFolderRelativePath: String): String {
            return "-M $mspFolderRelativePath"
        }

        fun register(entity: Entity) = apply {
            this.result += buildString {
                append(wrap {
                    "${fabricCAClient.register} --id.name ${entity.name} --id.secret ${buildPassword(entity)} --caname ${ca.name} --id.type ${entity.type} --tls.certfiles ../fabric-ca-server/tls-cert.pem"
                })
            } + "\n"
        }

        fun enroll(entity: Entity, mspFolderRelativePath: String, tlsFolderRelativePathFromFabricCaClientFolder: String? = null, tlsFolderPath: String? = null) = apply {
            this.result += buildString {
                append(wrap {
                    "${fabricCAClient.enroll} ${url(entity.name, buildPassword(entity))} ${csr(entity)} ${msp(mspFolderRelativePath)} --tls.certfiles ../fabric-ca-server/tls-cert.pem "
                })
                if (entity !is Client && tlsFolderRelativePathFromFabricCaClientFolder != null && tlsFolderPath != null) {
                    append("\n" + wrap {
                        "${fabricCAClient.enroll} ${url(entity.name, buildPassword(entity))} ${csr(entity)} ${tls(tlsFolderRelativePathFromFabricCaClientFolder) } --tls.certfiles ../fabric-ca-server/tls-cert.pem"
                    })
                    append("\n" + buildString {
                        append("cp ${Path.of(tlsFolderPath, "tlscacerts", "*")}  ${Path.of(tlsFolderPath, TlsFolder.caCrt)}\n")
                        append("cp ${Path.of(tlsFolderPath, "signcerts", "*")} ${Path.of(tlsFolderPath, TlsFolder.serverCrt)}\n")
                        append("cp ${Path.of(tlsFolderPath, "keystore", "*")} ${Path.of(tlsFolderPath, TlsFolder.serverKey)}\n")
                        appendNewline("mkdir -p ${Path.of("./msp/tlscacerts")}")
                        appendNewline("cp ${Path.of(tlsFolderPath, "tlscacerts", "*")} ${Path.of("./msp/tlscacerts", "tls-$url-$port-${ca.name}.pem")}")
                        appendNewline("mkdir ${Path.of("./tls", "/tlscacerts")}")
                        appendNewline("cp ${Path.of(tlsFolderPath, "tlscacerts", "*")} ${Path.of("./tls", "/tlscacerts", "tls-$url-$port-${ca.name}.pem")}")
                        appendNewline("mkdir ${Path.of( "./ca")}")
                        appendNewline("cp ${Path.of("./msp/cacerts", "*")} ${Path.of( "./ca", "tls-$url-$port-${ca.name}.pem")}")
                    })
                }
            }.plus("\n")
        }

        fun enroll() = apply {
            val identity = CAServerConfig.Identity(ca.username, ca.password)
            this.result += buildString {
                append(wrap {
                    "${fabricCAClient.enroll} ${url(identity.name, identity.password) } --caname ${ca.name} --tls.certfiles ../fabric-ca-server/tls-cert.pem"
                })
            }
        }


        override fun build(): String {
            return RegisterEnrollSh().toString() + this.exportHome() + this.result
        }
    }
}

fun RegisterEnrollSh.Companion.enrollCaAdmin(ca: Ca, fabricCARelativePath: String): String {
    return RegisterEnrollSh.RegisterEnrollBuilder(fabricCARelativePath, ca).enroll().build()
}

fun RegisterEnrollSh.Companion.registerEnrollEntity(entity: Entity, ca: Ca, fabricCARelativePath: String, mspFolderRelativePath: String, tlsFolderRelativePath: String? = null, tlsFolderPath: String? = null): String {
    return RegisterEnrollSh.RegisterEnrollBuilder(fabricCARelativePath, ca).register(entity).enroll(entity, mspFolderRelativePath, tlsFolderRelativePath, tlsFolderPath).build()
}
