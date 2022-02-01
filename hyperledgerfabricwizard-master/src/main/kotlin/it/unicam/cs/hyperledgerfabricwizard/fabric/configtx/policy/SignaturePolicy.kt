package it.unicam.cs.hyperledgerfabricwizard.fabric.configtx.policy

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonValue
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.util.StdConverter

enum class HyperledgerSignaturePolicy {
    READERS, WRITERS, ADMINS, ENDORSEMENT;

    companion object {
        fun admins(mspid: String): Pair<String, SignaturePolicy> {
            return ADMINS.toString() to SignaturePolicy(SignatureRule(SignatureRule.Operator.OR, listOf(
                    mspid to SignatureRule.Role.ADMIN
            )))
        }

        fun readers(mspid: String): Pair<String, SignaturePolicy> {
            return READERS.toString() to SignaturePolicy(SignatureRule(SignatureRule.Operator.OR, listOf(
                    mspid to SignatureRule.Role.MEMBER
            )))
        }

        fun writers(mspid: String): Pair<String, SignaturePolicy> {
            return WRITERS.toString() to SignaturePolicy(SignatureRule(SignatureRule.Operator.OR, listOf(
                    mspid to SignatureRule.Role.MEMBER
            )))
        }

        fun endorsement(mspid: String): Pair<String, SignaturePolicy> {
            return ENDORSEMENT.toString() to SignaturePolicy(SignatureRule(SignatureRule.Operator.OR, listOf(
                    mspid to SignatureRule.Role.PEER
            )))
        }
    }

    override fun toString(): String {
        return super.toString().toLowerCase().capitalize()
    }
}


@JsonDeserialize
class SignaturePolicy(
        @JsonProperty("Rule")
        override val rule: SignatureRule) : Policy() {
    @JsonProperty("Type")
    override val type: Type = Type.SIGNATURE
}


@JsonDeserialize(converter = SignatureRule.SignatureRuleDeserializer::class)
class SignatureRule(val operator: Operator, val entities: List<Pair<String, Role>>) : PolicyRule {
    class SignatureRuleDeserializer : StdConverter<String, SignatureRule>() {
        override fun convert(value: String): SignatureRule {
            val array = value.split(PREFIX)
            val operator = Operator.valueOf(array[0])
            val string = array[1].dropLast(1)
            val list = string.split(SEPARATOR_1).map {
                val array = it.trim().drop(1).dropLast(1).split(SEPARATOR_2)
                array[0] to Role.valueOf(array[1].toUpperCase())
            }
            return SignatureRule(operator, list)
        }

    }

    enum class Operator { AND, OR }
    enum class Role {
        CLIENT, PEER, ADMIN, ORDERER, MEMBER;

        @JsonValue
        override fun toString(): String {
            return super.toString().toLowerCase()
        }
    }

    companion object {
        const val PREFIX = "("
        const val POSTFIX = ")"
        const val SEPARATOR_1 = ","
        const val SEPARATOR_2 = "."
    }

    @JsonValue
    override fun toString(): String {
        return "$operator" + entities.joinToString(prefix = PREFIX, postfix = POSTFIX, separator = SEPARATOR_1) {
            "'${it.first}$SEPARATOR_2${it.second}'"
        }
    }
}