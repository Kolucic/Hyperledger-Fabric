package it.unicam.cs.hyperledgerfabricwizard.fabric.folder

import it.unicam.cs.hyperledgerfabricwizard.client.Ca
import it.unicam.cs.hyperledgerfabricwizard.fabric.scripts.Roles
import it.unicam.cs.hyperledgerfabricwizard.utils.Folder
import it.unicam.cs.hyperledgerfabricwizard.utils.ToFile

/**
 * La cartella conterrà i file crittografici.
 */
class MspFolder(folder: Folder, ca: Ca) : Folder(folder, "msp") {
    @ToFile("config.yaml")
    val nodeOUs = Roles(ca)
}