package it.unicam.cs.hyperledgerfabricwizard.fabric.configtx.policy

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonValue
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.util.StdConverter

class TypeDeserializer : StdConverter<String, Type>() {
    override fun convert(value: String): Type {
        return Type.valueOf(value.toUpperCase())
    }
}

@JsonDeserialize(converter = TypeDeserializer::class)
enum class Type {
    SIGNATURE {
        @JsonValue
        override fun toString(): String {
            return "Signature"
        }
    },
    IMPLICITMETA {
        @JsonValue
        override fun toString(): String {
            return "ImplicitMeta"
        }
    }
}

interface PolicyRule

class PolicyDeserializer : StdDeserializer<Policy>(Policy::class.java) {
    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): Policy {
        val mapper = p.codec as ObjectMapper
        val node: JsonNode = mapper.readTree(p)
        return when (mapper.treeToValue(node.get("Type"), Type::class.java)!!) {
            Type.IMPLICITMETA -> {
                mapper.treeToValue(node, ImplicityMetaPolicy::class.java)
            }
            Type.SIGNATURE -> {
                mapper.treeToValue(node, SignaturePolicy::class.java)
            }
        }
    }
}

@JsonDeserialize(using = PolicyDeserializer::class)
abstract class Policy {
    @get:JsonProperty("Type")
    abstract val type: Type

    @get:JsonProperty("Rule")
    abstract val rule: PolicyRule
}