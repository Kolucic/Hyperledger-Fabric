package it.unicam.cs.hyperledgerfabricwizard.fabric.configtx

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import it.unicam.cs.hyperledgerfabricwizard.fabric.configtx.policy.ImplicitMetaRule
import it.unicam.cs.hyperledgerfabricwizard.fabric.configtx.policy.ImplicityMetaPolicy
import it.unicam.cs.hyperledgerfabricwizard.fabric.configtx.policy.Policy

@JsonInclude(JsonInclude.Include.NON_NULL)
//@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator::class)
class Application(
        @JsonProperty("Organizations") val organizations: List<Organization>? = null,
        @JsonProperty("Policies") val policies: Map<String, Policy>,
        @JsonProperty("Capabilities") val capabilities: Capability
) {
    companion object {
        fun default(organizations: List<Organization>, capabilities: Capability): Application {
            val policies = mapOf(
                    "Readers" to ImplicityMetaPolicy(ImplicitMetaRule(ImplicitMetaRule.Operator.ANY, "Readers")),
                    "Writers" to ImplicityMetaPolicy(ImplicitMetaRule(ImplicitMetaRule.Operator.ANY, "Writers")),
                    "Admins" to ImplicityMetaPolicy(ImplicitMetaRule(ImplicitMetaRule.Operator.MAJORITY, "Admins")),
                    "LifecycleEndorsement" to ImplicityMetaPolicy(ImplicitMetaRule(ImplicitMetaRule.Operator.MAJORITY, "Endorsement")),
                    "Endorsement" to ImplicityMetaPolicy(ImplicitMetaRule(ImplicitMetaRule.Operator.MAJORITY, "Endorsement"))
            )
            return Application(organizations, policies, capabilities)
        }
    }
}