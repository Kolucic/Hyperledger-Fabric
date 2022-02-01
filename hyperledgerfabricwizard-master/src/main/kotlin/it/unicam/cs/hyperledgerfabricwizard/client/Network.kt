package it.unicam.cs.hyperledgerfabricwizard.client

/**
 * La classe identifica la rete HyperledgerFabric da creare.
 *
 * È composta di un nome [name] e un insieme di [Org],
 * un insieme di consorzi [consortiums] e un insieme di canali [channels].
 */
data class Network(
        val name: String,
        val orgs: List<Org>,
        val consortiums: List<Consortium>,
        val channels: List<Channel>
)


