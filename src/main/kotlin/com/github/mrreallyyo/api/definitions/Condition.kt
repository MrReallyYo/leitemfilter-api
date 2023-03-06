package com.github.mrreallyyo.api.definitions

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement

@JacksonXmlRootElement(localName = "Condition")
data class Condition(
    @JacksonXmlProperty(isAttribute = true, localName = "i:type")
    var i_type: String? = null,
    // AffixCondition
    var affixes: Affixes? = null,
    // SubTypeCondition
    var type: Type? = null,
    var subTypes: SubTypes? = null,
    // ClassCondition
    var req: String? = null,
    // RarityCondition
    var rarity: String? = null,

    var comparsion: String? = null,
    var comparsionValue: Int? = null,
    var minOnTheSameItem: Int? = null,
    var combinedComparsion: String? = null,
    var combinedComparsionValue: Int? = null,
    var advanced: Boolean? = null


) {

    val isAffixCondition: Boolean = i_type == "AffixCondition"
    val isBaseCondition: Boolean = i_type == "SubTypeCondition"
}