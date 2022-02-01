package it.unicam.cs.hyperledgerfabricwizard.fabric.folder

import it.unicam.cs.hyperledgerfabricwizard.client.Ca
import it.unicam.cs.hyperledgerfabricwizard.client.Entity
import it.unicam.cs.hyperledgerfabricwizard.utils.Directories
import it.unicam.cs.hyperledgerfabricwizard.utils.Folder

class EntitiesFolder(folder: Folder, entities: List<Entity>, ca: Ca, fabricCAClientFolder: FabricCAClientFolder) : Folder(folder, "entities") {
    @Directories
    val entityFolders: List<EntityFolder> = entities.map {
        EntityFolder.newInstance(this, it, ca, fabricCAClientFolder)
    }
}