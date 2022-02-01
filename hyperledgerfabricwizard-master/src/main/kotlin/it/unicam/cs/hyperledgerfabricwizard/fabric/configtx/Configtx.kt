package it.unicam.cs.hyperledgerfabricwizard.fabric.configtx

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import it.unicam.cs.hyperledgerfabricwizard.utils.fromJson
import it.unicam.cs.hyperledgerfabricwizard.utils.fromYaml
import it.unicam.cs.hyperledgerfabricwizard.utils.toJson
import it.unicam.cs.hyperledgerfabricwizard.utils.toYaml
import javax.xml.stream.events.Characters

/**
 * La classe rappresenta il file configtx.yaml che permette di creare il canale di sistema e i vari canali
 * specificati dall'oggetto [it.unicam.cs.hyperledgerfabricwizard.client.Network].
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
class Configtx(
        @JsonProperty("Organizations") val organizations: List<Organization>?,
        @JsonProperty("Capabilities") val capabilities: Capabilities?,
        @JsonProperty("Application") val application: Application?,
        @JsonProperty("Orderer") val orderer: Orderer?,
        @JsonProperty("Channel") val channel: Channel?,
        @JsonProperty("Profiles") val profiles: Map<String, Profile>
) {
    override fun toString(): String {
        val x = this.toYaml()
        var n = x
        "&\\w+\\s+".toRegex().findAll(x).forEach {
            n = n.replace(it.value, it.value + "\n  ")
        }
        return n
    }
}