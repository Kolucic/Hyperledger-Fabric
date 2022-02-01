package it.unicam.cs.hyperledgerfabricwizard

import it.unicam.cs.hyperledgerfabricwizard.client.*
import it.unicam.cs.hyperledgerfabricwizard.fabric.folder.NetworkFolder
import it.unicam.cs.hyperledgerfabricwizard.utils.fromJson
import it.unicam.cs.hyperledgerfabricwizard.utils.toJson
import org.junit.jupiter.api.Test
import java.io.File

val org1: Org = run {
    val entities = mutableListOf<Entity>()
    val org1 = Org("org1", "example.com", entities)
    org1.ca = Ca("ca-org1", url = "localhost", port = 7054).apply { org = org1 }
    val peer0 = Peer("peer0", org1.toString(), /*"peer0pw",*/ true, "localhost", 7051)
    val admin = Client("org1admin", org1.toString(), /*"org1adminpw",*/ true)
    //val orderer = Orderer("orderer1", org1.toString(), "orderer1pw", "localhost", 7090)
    val user1 = Client("user1", org1.toString()/*, "user1pw"*/)
    entities += peer0
    entities += admin
    entities += user1
    org1
}

val org2: Org = run {
    val entities = mutableListOf<Entity>()
    val org2 = Org("org2", "example.com", entities)
    org2.ca = Ca("ca-org2", url = "localhost", port = 8054).apply { org = org2 }
    val peer0 = Peer("peer0", org2.toString(), /*"peer0pw",*/ true, "localhost", 9051)
    //val peer1 = Peer("peer1", org2.toString(), "peer1pw", true, "localhost", 9052)
    val admin = Client("org2admin", org2.toString(), /*"org2adminpw",*/ true)
    val user1 = Client("user1", org2.toString()/*, "user1pw"*/)
    entities += peer0
    //entities += peer1
    entities += admin
    entities += user1
    org2
}

val ordererOrg = run {
    val entities = mutableListOf<Entity>()
    val ordererOrg = Org("ordererOrg", "example.com", entities)
    ordererOrg.ca = Ca("ca-orderer", url = "localhost", port = 9054).apply { org = ordererOrg }
    val orderer = Orderer("orderer", ordererOrg.toString(), "localhost", 7050)
    val admin = Client("ordererAdmin", ordererOrg.toString(), /*"ordererAdminpw",*/ true)
    entities += orderer
    entities += admin
    ordererOrg
}

val sampleConsortium = Consortium("SampleConsortium", listOf(org1, org2))
val myChannel = Channel("mychannel", sampleConsortium, listOf(org1, org2))
val testNetwork = Network("test-network", listOf(org1, org2, ordererOrg), listOf(sampleConsortium), listOf(myChannel))

class TestNetwork {
    @Test
    fun testNetwork() {
        val rootFolder = NetworkFolder("/home/luca/Scaricati/", testNetwork)
        rootFolder.walkTree {
            println(it)
            it.createFolder()
            it.folderFiles { file, content ->
                println(file.path)
                file.writeText(content)
            }
        }
        //File("/home/luca/Scaricati/${testNetwork.name}.zip").writeBytes(zip(File(rootFolder.path)));
    }

    @Test
    fun test() {
        val x = testNetwork.toJson()
        File("/home/luca/Scaricati/a.json").writeText(x)
        val n = x.fromJson<Network>()
        println(n)
    }
}