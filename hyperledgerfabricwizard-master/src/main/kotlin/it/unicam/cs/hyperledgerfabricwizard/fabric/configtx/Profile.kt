package it.unicam.cs.hyperledgerfabricwizard.fabric.configtx

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import it.unicam.cs.hyperledgerfabricwizard.fabric.configtx.policy.Policy

@JsonInclude(JsonInclude.Include.NON_NULL)
class Profile(
        @JsonProperty("Policies") val policies: Map<String, Policy>? = null,
        @JsonProperty("Capabilities") val capabilities: Capability? = null,
        @JsonProperty("Orderer") val orderer: Orderer? = null,
        @JsonProperty("Consortium") val consortium: String? = null,
        @JsonProperty("Consortiums") val consortiums: Map<String, Consortium>? = null,
        @JsonProperty("Application") val application: Application? = null
)