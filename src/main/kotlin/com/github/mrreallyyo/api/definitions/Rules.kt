package com.github.mrreallyyo.api.definitions

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty

data class Rules(
    @JacksonXmlProperty(localName = "Rule")
    @JacksonXmlElementWrapper(useWrapping = false)
    var rule: List<Rule>? = null
)

class RulesFilter {
    override fun equals(other: Any?): Boolean {
        val rules = other as? Rules ?: return other == null
        return rules.rule == null || rules.rule?.isEmpty() == true
    }
}