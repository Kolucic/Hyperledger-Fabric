package it.unicam.cs.hyperledgerfabricwizard.fabric.folder

import it.unicam.cs.hyperledgerfabricwizard.client.Org
import it.unicam.cs.hyperledgerfabricwizard.fabric.Globals.FABRIC_CA_CLIENT_CONFIG
import it.unicam.cs.hyperledgerfabricwizard.fabric.ca.CAClientConfig
import it.unicam.cs.hyperledgerfabricwizard.utils.Directory
import it.unicam.cs.hyperledgerfabricwizard.utils.Folder
import it.unicam.cs.hyperledgerfabricwizard.utils.ToFile

class FabricCAClientFolder(folder: Folder, org: Org, fabricCAServerFolder: FabricCAServerFolder) : Folder(folder, "fabric-ca-client") {
    @Directory
    val mspFolder = MspFolder(this, org.ca)

    @ToFile(FABRIC_CA_CLIENT_CONFIG)
    val fabricCAClientConfig: CAClientConfig = CAClientConfig(org.ca, this.relativePathTo(fabricCAServerFolder))

    init {
        org.mspFolder = this.mspFolder
    }
}
