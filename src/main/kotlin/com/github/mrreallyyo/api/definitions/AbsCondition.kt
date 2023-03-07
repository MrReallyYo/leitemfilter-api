package com.github.mrreallyyo.api.definitions

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.TreeNode
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.node.TextNode
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement

@JsonDeserialize(using = AbsConditionDeserializer::class)
@JacksonXmlRootElement(localName = "Condition")
open class AbsCondition {
    @JacksonXmlProperty(isAttribute = true, localName = "i:type")
    var i_type: String? = null

    @get:JsonIgnore
    val isAffixCondition: Boolean
        get() {
            return this is AffixCondition
        }
    @get:JsonIgnore
    val isClassCondition: Boolean
        get() {
            return this is ClassCondition
        }
    @get:JsonIgnore
    val isLevelCondition: Boolean
        get() {
            return this is LevelCondition
        }
    @get:JsonIgnore
    val isRarityCondition: Boolean
        get() {
            return this is RarityCondition
        }
    @get:JsonIgnore
    val isSubTypeCondition: Boolean
        get() {
            return this is SubTypeCondition
        }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AbsCondition

        if (i_type != other.i_type) return false

        return true
    }

    override fun hashCode(): Int {
        return i_type?.hashCode() ?: 0
    }


}

class AbsConditionDeserializer : StdDeserializer<AbsCondition>(AbsCondition::class.java) {
    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): AbsCondition? {
        val node: TreeNode = p.readValueAsTree()

        val textNode = node.get("i:type") as? TextNode ?: return null


        val clazz = when (textNode.asText()) {
            "AffixCondition" -> AffixCondition::class
            "ClassCondition" -> ClassCondition::class
            "LevelCondition" -> LevelCondition::class
            "RarityCondition" -> RarityCondition::class
            "SubTypeCondition" -> SubTypeCondition::class
            else -> return null
        }


        return p.codec.treeToValue(node, clazz.java)
    }
}