package com.github.mrreallyyo.api.definitions


import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement

@JacksonXmlRootElement(localName = "Rule")
data class Rule(
    var type: String? = null,
    var color: Int? = null,
    var isEnabled: Boolean? = null,
    var levelDependent: Boolean? = null,
    var minLvl: Int? = null,
    var maxLvl: Int? = null,
    var emphasized: Boolean? = null,
    var nameOverride: String? = null,
    var conditions: Conditions? = null
) {
    val isFixRule: Boolean = (conditions?.condition?.size ?: 0) > 1 || !nameOverride.isNullOrBlank()
    val isAffixRule: Boolean =
        !isFixRule && conditions?.condition?.size == 1 && conditions!!.condition!!.first().i_type == "AffixCondition"
    val isBaseRule: Boolean =
        !isFixRule && conditions?.condition?.size == 1 && conditions!!.condition!!.first().i_type == "SubTypeCondition"

    fun recolor(color: Int) {
        this.color = color
        type = "HIGHLIGHT"
    }
}