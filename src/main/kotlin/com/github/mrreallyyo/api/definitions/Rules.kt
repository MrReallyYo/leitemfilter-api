package com.github.mrreallyyo.api.definitions

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty

data class Rules(
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "Rule")
    var rule: List<Rule> = emptyList()
)

class RulesFilter {
    override fun equals(other: Any?): Boolean {
        val rules = other as? Rules ?: return other == null
        return rules.rule.isEmpty()
    }
}