package it.unicam.cs.hyperledgerfabricwizard.fabric.configtx

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.util.StdConverter

class Capabilities(
        @JsonProperty("Channel") val channel: Capability,
        @JsonProperty("Orderer") val orderer: Capability,
        @JsonProperty("Application") val application: Capability
) {
    companion object {
        fun default(): Capabilities {
            val capability = Capability("V2_0", true)
            return Capabilities(capability, capability, capability)
        }
    }
}

@JsonSerialize(converter = Capability.CapabilitySerializer::class)
@JsonDeserialize(converter = Capability.CapabilityDeserializer::class)
class Capability(
        val version: String,
        val accepted: Boolean
) {
    class CapabilitySerializer : StdConverter<Capability, Map<String, Boolean>>() {
        override fun convert(value: Capability): Map<String, Boolean> {
            return mapOf(Pair(value.version, value.accepted))
        }
    }

    class CapabilityDeserializer : StdConverter<Map<String, Boolean>, Capability>() {
        override fun convert(value: Map<String, Boolean>): Capability {
            val pair = value.map { Pair(it.key, it.value) }.first()
            return Capability(pair.first, pair.second)
        }
    }

}