package com.github.mrreallyyo.api.definitions

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty

data class Conditions(
    @JacksonXmlProperty(localName = "Condition")
    @JacksonXmlElementWrapper(useWrapping = false)
    var condition: List<AbsCondition>? = null
)

class ConditionsFilter {
    override fun equals(other: Any?): Boolean {
        val conditions = other as? Conditions ?: return other == null
        return conditions.condition == null || conditions.condition?.isEmpty() == true
    }
}