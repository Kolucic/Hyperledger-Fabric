package it.unicam.cs.hyperledgerfabricwizard.fabric.folder

import it.unicam.cs.hyperledgerfabricwizard.client.Channel
import it.unicam.cs.hyperledgerfabricwizard.client.Network
import it.unicam.cs.hyperledgerfabricwizard.fabric.scripts.CreateChannelSh
import it.unicam.cs.hyperledgerfabricwizard.fabric.scripts.JoinChannelSh
import it.unicam.cs.hyperledgerfabricwizard.utils.Folder
import it.unicam.cs.hyperledgerfabricwizard.utils.ToFile

class ChannelFolder(folder: Folder, network: Network, channel: Channel, networkFolder: NetworkFolder) : Folder(folder, channel.name) {
    @ToFile("create.sh")
    val createChannelSh = CreateChannelSh(this, network, channel, networkFolder)

    @ToFile("join.sh")
    val joinChannelSh = JoinChannelSh(this, network, channel, networkFolder)
}