package it.unicam.cs.hyperledgerfabricwizard.fabric.configtx

import com.fasterxml.jackson.annotation.JsonProperty
import it.unicam.cs.hyperledgerfabricwizard.fabric.configtx.policy.ImplicitMetaRule
import it.unicam.cs.hyperledgerfabricwizard.fabric.configtx.policy.ImplicityMetaPolicy
import it.unicam.cs.hyperledgerfabricwizard.fabric.configtx.policy.Policy

//@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator::class)
class Channel(
        @JsonProperty("Policies") val policies: Map<String, Policy>,
        @JsonProperty("Capabilities") val capabilities: Capability
) {
    companion object {
        fun default(capabilities: Capability): Channel {
            val policies = mapOf(
                    "Readers" to ImplicityMetaPolicy(ImplicitMetaRule(ImplicitMetaRule.Operator.ANY, "Readers")),
                    "Writers" to ImplicityMetaPolicy(ImplicitMetaRule(ImplicitMetaRule.Operator.ANY, "Writers")),
                    "Admins" to ImplicityMetaPolicy(ImplicitMetaRule(ImplicitMetaRule.Operator.MAJORITY, "Admins"))
            )
            return Channel(policies, capabilities)
        }
    }
}