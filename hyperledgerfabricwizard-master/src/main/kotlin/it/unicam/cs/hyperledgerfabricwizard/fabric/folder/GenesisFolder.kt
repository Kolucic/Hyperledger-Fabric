package it.unicam.cs.hyperledgerfabricwizard.fabric.folder

import it.unicam.cs.hyperledgerfabricwizard.fabric.Globals
import it.unicam.cs.hyperledgerfabricwizard.utils.Folder

class GenesisFolder(folder: Folder) : Folder(folder, "system-genesis-block") {
    val genesisBlockPath = buildPath(Globals.GENESIS_BLOCK)
}
