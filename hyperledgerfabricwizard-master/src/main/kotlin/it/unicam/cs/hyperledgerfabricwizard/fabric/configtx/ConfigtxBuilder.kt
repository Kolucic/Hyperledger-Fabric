package it.unicam.cs.hyperledgerfabricwizard.fabric.configtx

import it.unicam.cs.hyperledgerfabricwizard.client.Network
import it.unicam.cs.hyperledgerfabricwizard.client.Org
import it.unicam.cs.hyperledgerfabricwizard.client.Peer
import it.unicam.cs.hyperledgerfabricwizard.fabric.Globals
import it.unicam.cs.hyperledgerfabricwizard.fabric.configtx.policy.HyperledgerSignaturePolicy
import it.unicam.cs.hyperledgerfabricwizard.fabric.folder.TlsFolder
import it.unicam.cs.hyperledgerfabricwizard.utils.Builder
import it.unicam.cs.hyperledgerfabricwizard.utils.Folder
import java.nio.file.Path
import it.unicam.cs.hyperledgerfabricwizard.client.Orderer as OrdererClient

/**
 * La classe permette di creare un file configtx.yaml
 *
 * Crea un profilo per ogni canale presente nell'oggetto network.
 */
class ConfigtxBuilder(private val network: Network, private val folder: Folder) : Builder<Configtx> {
    private val organizations: Map<String, Organization>
    private val capabilities = Capabilities.default()
    private val channel = Channel.default(capabilities.channel)
    private val orderer: Orderer
    private val consortiums: Map<String, Consortium>
    private val profiles: Map<String, Profile>

    /**
     * Crea la sezione Organizations del configtx.yaml
     */
    private fun createOrg(org: Org): Organization {
        val name = org.configtxName
        val mspID = org.alternativeName
        var peerCount = 0
        val anchorPeers = org.entities.filter {
            if (it is Peer) {
                peerCount++
                it.isAnchor
            } else false
        }.map {
            it as Peer;
            val url = if (it.isLocalhost()) it.toString() else it.url
            Address(url, it.port)
        }.ifEmpty { null }
        val ordererEndpoints = org.entities.filterIsInstance<OrdererClient>().map {
            val url = if (it.isLocalhost()) it.toString() else it.url
            "$url:${it.port}"
        }.ifEmpty { null }
        val policies = mutableMapOf(
                HyperledgerSignaturePolicy.writers(mspID),
                HyperledgerSignaturePolicy.readers(mspID),
                HyperledgerSignaturePolicy.admins(mspID)
        )
        if (peerCount > 0) {
            policies += HyperledgerSignaturePolicy.endorsement(mspID)
        }

        return Organization(name, mspID, this.folder.relativePathTo(org.mspFolder), policies, anchorPeers, ordererEndpoints)
    }

    /**
     * Crea il profilo del canale di sistema,
     * Aggiunge i consorzi, l'ordering service
     */
    private fun systemProfile(): Pair<String, Profile> {
        return Globals.SYSTEM_CHANNEL_PROFILE to Profile(
                orderer = orderer,
                policies = channel.policies,
                capabilities = channel.capabilities,
                consortiums = consortiums
        )
    }

    /**
     * Genero il profilo per un canale
     */
    private fun channelProfiles(): List<Pair<String, Profile>> {
        return this.network.channels.map { c ->
            // Aggiungo le org del canale filtrando per quelle che fanno parte del consorzio
            // Perché nella sezione Application/Organizations di un canale
            // Possono comparire solo le org che fanno parte del consorzio nel genesis block
            val orgs = c.orgs.filter {
                c.consortium.orgs.contains(it)
            }.map {
                this.organizations[it.toString()]!!
            }
            c.name to Profile(
                    policies = channel.policies,
                    capabilities = channel.capabilities,
                    consortium = c.consortium.name,
                    application = Application.default(
                            orgs, capabilities.application)
            )
        }
    }

    init {
        this.organizations = this.network.orgs.map { Pair(it.toString(), createOrg(it)) }.toMap()
        /**
         * Aggiunge all'ordering service tutti gli orderer che trova sulla rete
         */
        val consenters = this.network.orgs.flatMap { it.entities.filterIsInstance<OrdererClient>() }.map {
            val path = Path.of(this.folder.relativePathTo(it.tlsFolder), TlsFolder.serverCrt).toString()
            Address(it.toString(), it.port, path, path)
        }
        this.orderer = Orderer.default(consenters, capabilities.orderer, this.organizations.values.filter { it.ordererEndpoints != null })
        this.consortiums = this.network.consortiums.map { it.name to Consortium(it.orgs.map { this.organizations[it.toString()]!! }) }.toMap()
        this.profiles = mutableMapOf(systemProfile()).apply { putAll(channelProfiles()) }
    }

    override fun build(): Configtx {
        return Configtx(
                this.organizations.values.toList(),
                null,
                null,
                null,
                null,
                this.profiles
        )
    }
}