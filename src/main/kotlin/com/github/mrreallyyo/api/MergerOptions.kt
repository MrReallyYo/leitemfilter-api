package com.github.mrreallyyo.api

import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.github.mrreallyyo.api.definitions.ItemFilter

data class MergerOptions(
    val header: ItemFilter? = null,
    val useEmbeddedHeader: Boolean = defaultUseEmbeddedHeader,
    val footer: ItemFilter? = null,
    val useEmbeddedFooter: Boolean = defaultUseEmbeddedFooter,
    val baseFilters: List<ItemFilter>,
    val generateRules: Boolean = defaultGenerateRules,
    val multiplayerColors: List<Int> = defaultMultiplayerColors,
    val overrideColors: Boolean = defaultOverrideColors,
    val ruleLimit: Int = defaultRuleLimit,
    val enforceRuleLimit: Boolean = defaultEnforceRuleLimit,
    var mapper: XmlMapper = ItemFilterMapper.newMapper()
) {

    companion object {

        const val defaultUseEmbeddedHeader = true
        const val defaultUseEmbeddedFooter = true
        const val defaultGenerateRules = true
        val defaultMultiplayerColors = listOf(17, 14, 12)
        const val defaultOverrideColors = true
        const val defaultRuleLimit = 75
        const val defaultEnforceRuleLimit = true
    }


}