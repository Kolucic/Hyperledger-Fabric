package it.unicam.cs.hyperledgerfabricwizard.client

import com.fasterxml.jackson.annotation.JsonIdentityInfo
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonManagedReference
import com.fasterxml.jackson.annotation.ObjectIdGenerators
import it.unicam.cs.hyperledgerfabricwizard.fabric.folder.FabricCAClientFolder
import it.unicam.cs.hyperledgerfabricwizard.fabric.folder.FabricCAServerFolder
import it.unicam.cs.hyperledgerfabricwizard.utils.Folder


/**
 * Identifica l'organizzazione in una rete HyperledgerFabric.
 *
 * L'organizzazione è composta da nome [name], dominio [domain], entitià [entities], e il certificate authority [ca].
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator::class, property = "fullName")
data class Org(
        val name: String,
        val domain: String,
        val entities: List<Entity>,
        val fullName: String = "$name.$domain"
) {
    @JsonManagedReference
    lateinit var ca: Ca

    @JsonIgnore
    val alternativeName = fullName.replace(".", "-")

    @JsonIgnore
    val configtxName = fullName.replace(".", "")

    @JsonIgnore
    lateinit var mspFolder: Folder

    @JsonIgnore
    lateinit var caServerFolder: FabricCAServerFolder

    @JsonIgnore
    lateinit var caClientFolder: FabricCAClientFolder

    override fun toString(): String {
        return this.fullName
    }

    override fun equals(other: Any?): Boolean {
        if (other == null) return false
        if (other is Org) {
            return this.fullName == other.fullName
        }
        return false
    }

    override fun hashCode(): Int {
        return fullName.hashCode()
    }
}

