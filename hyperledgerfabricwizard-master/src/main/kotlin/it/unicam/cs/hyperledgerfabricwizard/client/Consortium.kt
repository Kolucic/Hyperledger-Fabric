package it.unicam.cs.hyperledgerfabricwizard.client

import com.fasterxml.jackson.annotation.JsonIdentityInfo
import com.fasterxml.jackson.annotation.ObjectIdGenerators

/**
 * Rappresenta un insieme di organizzazioni.
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator::class, property = "name")
data class Consortium(
        val name: String = "",
        val orgs: List<Org>
)