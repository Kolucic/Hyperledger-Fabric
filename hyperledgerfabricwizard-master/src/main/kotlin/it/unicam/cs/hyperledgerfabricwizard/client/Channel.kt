package it.unicam.cs.hyperledgerfabricwizard.client


/**
 * Rappresenta un canale all'interno della rete.
 *
 * Un canale deve avere un nome [name], il consorzio che lo amministra [consortium]
 * e un insieme di organizzazioni che ne faranno parte.
 */
class Channel(
        val name: String,
        val consortium: Consortium,
        val orgs: List<Org>
)