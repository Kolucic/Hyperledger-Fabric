package it.unicam.cs.hyperledgerfabricwizard.fabric.configtx.policy

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonValue
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.util.StdConverter

@JsonDeserialize
class ImplicityMetaPolicy(@JsonProperty("Rule") override val rule: ImplicitMetaRule) : Policy() {
    @JsonProperty("Type")
    override val type: Type = Type.IMPLICITMETA
}

@JsonDeserialize(converter = ImplicitMetaRuleDeserializer::class)
class ImplicitMetaRule(val operator: Operator, val policyName: String) : PolicyRule {
    enum class Operator { ALL, ANY, MAJORITY }

    @JsonValue
    override fun toString(): String {
        return "$operator $policyName"
    }
}

class ImplicitMetaRuleDeserializer : StdConverter<String, ImplicitMetaRule>() {
    override fun convert(value: String): ImplicitMetaRule {
        val array = value.split(" ")
        val operator = ImplicitMetaRule.Operator.valueOf(array[0])
        val policyName = array[1]
        return ImplicitMetaRule(operator, policyName)
    }

}