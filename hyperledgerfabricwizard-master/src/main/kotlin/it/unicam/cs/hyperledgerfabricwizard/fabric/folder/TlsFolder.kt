package it.unicam.cs.hyperledgerfabricwizard.fabric.folder

import it.unicam.cs.hyperledgerfabricwizard.utils.Folder

class TlsFolder(folder: Folder) : Folder(folder, "tls") {
    companion object {
        const val serverCrt = "server.crt"
        const val caCrt = "ca.crt"
        const val tlscacert= ""
        const val serverKey = "server.key"
    }
}
