package it.unicam.cs.hyperledgerfabricwizard.fabric.configtx

import com.fasterxml.jackson.annotation.JsonIdentityInfo
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.ObjectIdGenerators
import it.unicam.cs.hyperledgerfabricwizard.fabric.configtx.policy.ImplicitMetaRule
import it.unicam.cs.hyperledgerfabricwizard.fabric.configtx.policy.ImplicityMetaPolicy
import it.unicam.cs.hyperledgerfabricwizard.fabric.configtx.policy.Policy

class BatchSize(
        @JsonProperty("MaxMessageCount") val maxMessageCount: Int,
        @JsonProperty("AbsoluteMaxBytes") val absoluteMaxBytes: String,
        @JsonProperty("PreferredMaxBytes") val preferredMaxBytes: String
)

class Raft(
        @JsonProperty("Consenters") val consenters: List<Address>
)

@JsonInclude(JsonInclude.Include.NON_NULL)
//@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator::class)
class Orderer(
        @JsonProperty("OrdererType") val ordererType: String,
        @JsonProperty("EtcdRaft") val etcdraft: Raft,
        @JsonProperty("BatchTimeout") val batchTimeout: String,
        @JsonProperty("BatchSize") val batchSize: BatchSize,
        @JsonProperty("Organizations") val organizations: List<Organization>? = null,
        @JsonProperty("Policies") val policies: Map<String, Policy>,
        @JsonProperty("Capabilities") val capabilities: Capability? = null
) {
    enum class Type {
        ETCDRAFT;

        override fun toString(): String {
            return super.toString().toLowerCase()
        }
    }

    companion object {
        fun default(consenters: List<Address>, capabilities: Capability, organizations: List<Organization>): Orderer {
            val ordererType = Type.ETCDRAFT.toString()
            val etcdraft = Raft(consenters)
            val batchTimeout = "2s"
            val batchSize = BatchSize(10, "99 MB", "512 KB")
            val policies = mapOf("Readers" to ImplicityMetaPolicy(ImplicitMetaRule(ImplicitMetaRule.Operator.ANY, "Readers")), "Writers" to ImplicityMetaPolicy(ImplicitMetaRule(ImplicitMetaRule.Operator.ANY, "Writers")), "Admins" to ImplicityMetaPolicy(ImplicitMetaRule(ImplicitMetaRule.Operator.MAJORITY, "Admins")), "BlockValidation" to ImplicityMetaPolicy(ImplicitMetaRule(ImplicitMetaRule.Operator.ANY, "Writers")))
            return Orderer(ordererType, etcdraft, batchTimeout, batchSize, organizations, policies, capabilities)
        }
    }
}

