package it.unicam.cs.hyperledgerfabricwizard.fabric.configtx

import com.fasterxml.jackson.annotation.JsonProperty

class Consortium(
        @JsonProperty("Organizations") val organizations: List<Organization>
)