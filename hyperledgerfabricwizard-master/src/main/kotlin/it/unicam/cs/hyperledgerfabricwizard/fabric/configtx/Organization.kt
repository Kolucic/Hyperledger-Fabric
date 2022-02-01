package it.unicam.cs.hyperledgerfabricwizard.fabric.configtx

import com.fasterxml.jackson.annotation.*
import it.unicam.cs.hyperledgerfabricwizard.fabric.configtx.policy.Policy

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator::class, property = "Name")
class Organization(
        @JsonProperty("Name") val name: String,
        @JsonProperty("ID") val id: String,
        @JsonProperty("MSPDir") val mspDir: String,
        @JsonProperty("Policies") val policies: Map<String, Policy>,
        @JsonProperty("AnchorPeers") val anchorPeers: List<Address>? = null,
        @JsonProperty("OrdererEndpoints") val ordererEndpoints: List<String>? = null
)


@JsonInclude(JsonInclude.Include.NON_NULL)
class Address(
        @JsonProperty("Host") val host: String,
        @JsonProperty("Port") val port: Int,
        @JsonProperty("ClientTLSCert") val clientTlsCert: String? = null,
        @JsonProperty("ServerTLSCert") val serverTlsCert: String? = null
)