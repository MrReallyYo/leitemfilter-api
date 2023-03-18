package com.github.mrreallyyo.api

import com.github.mrreallyyo.api.definitions.*
import mu.KotlinLogging
import java.util.*

class ItemFilterMerger(val options: MergerOptions) {

    private val logger = KotlinLogging.logger { }


    private val mapper = ItemFilterMapper.newMapper()

    private fun getHeader(): ItemFilter? {
        if (options.header != null) return options.header
        if (!options.useEmbeddedHeader) return null
        return ItemFilterMerger::class.java.classLoader.getResourceAsStream("_header.xml")!!.use {
            ItemFilter.load(it)
        }
    }

    private fun getFooter(): ItemFilter? {
        if (options.footer != null) return options.footer
        if (!options.useEmbeddedFooter) return null
        return ItemFilterMerger::class.java.classLoader.getResourceAsStream("_footer.xml")!!.use {
            ItemFilter.load(it)
        }
    }

    private fun getOverrideColors(): List<Int> {
        // dont override colors
        if (!options.overrideColors) return emptyList()
        val overrideColors = mutableListOf<Int>()
        if (options.multiplayerColors.size >= options.baseFilters.size - 1) {
            // we can match a color for each filter
            for (i in 0 until options.baseFilters.size - 1) {
                overrideColors.add(options.multiplayerColors[i])
            }
        } else if (options.multiplayerColors.isNotEmpty()) {
            // only use first color
            for (i in 0 until options.baseFilters.size - 1) {
                overrideColors.add(options.multiplayerColors.first())
            }
        }
        return overrideColors
    }


    private inline fun <reified T> clone(what: T): T {
        val store = mapper.writeValueAsString(what)
        return mapper.readValue(store, T::class.java)
    }

    fun mergeFilter(): ItemFilter {

        if (options.baseFilters.isEmpty()) {
            throw ItemFilterMergerException("No filter to work with.")
        }


        val rules = mutableListOf<Rule>()

        val mainFilter = clone(options.baseFilters.first())

        val colorsForOverride = getOverrideColors().toMutableList()

        options.baseFilters.reversed().forEach { currentFilter ->
            // check if this is the mainFilter
            val isMainFilter = currentFilter == options.baseFilters.first()
            logger.info { "Parsing ${currentFilter.fileName}." }

            val rulesToKeep = mutableListOf<Rule>()
            val affixRules = mutableListOf<Rule>()
            val baseRules = mutableListOf<Rule>()

            // sort rules
            (currentFilter.rules?.rule ?: emptyList()).forEach { rule ->
                rule.isMainFilter = isMainFilter
                if (!options.generateRules) {
                    // we just keep all enabled rules from all filters and disabled from main filter
                    if (rule.isEnabled == true || isMainFilter) {
                        rulesToKeep.add(rule)
                    }
                } else {
                    // sort rules for generation
                    when {
                        rule.isAffixRule -> if (rule.isEnabled == true) affixRules.add(rule)
                        rule.isSubTypeRule -> if (rule.isEnabled == true) baseRules.add(rule)
                        rule.isFixRule -> if (rule.isEnabled == true || isMainFilter) rulesToKeep.add(rule)
                    }
                }
            }

            logger.info { "Found ${rulesToKeep.size} rules to keep." }
            if (options.generateRules) {
                logger.info { "Found ${affixRules.size} affix rules." }
                logger.info { "Found ${baseRules.size} base rules." }
            }


            val overrideColor = colorsForOverride.removeLastOrNull()
            // build combined affix/base rules if enabled
            affixRules.forEach { affix ->
                baseRules.forEach { base ->
                    val conditions = mutableListOf<AbsCondition>()
                    affix.conditions?.condition?.firstOrNull()?.let { conditions.add(it) }
                    base.conditions?.condition?.firstOrNull()?.let { conditions.add(it) }

                    val combined = clone(affix)
                    combined.conditions = Conditions(conditions)

                    if (overrideColor != null) {
                        combined.recolor(overrideColor)
                    }
                    combined.generateRuleName(currentFilter.fileName)
                    rules.add(combined)
                }
            }

            // work on rules to keep
            rulesToKeep.forEach {
                if (overrideColor != null) {
                    it.recolor(overrideColor)
                }
                it.generateRuleName(currentFilter.fileName)
            }

            if (overrideColor != null) {
                rulesToKeep.forEach { it.recolor(overrideColor) }
            }

            // add the fixed rules
            rules.addAll(rulesToKeep)
        }



        rules.sortWith(RuleComparator())

        // check if we have a footer
        getFooter()?.let { footer ->
            rules.addAll(0, clone(footer).rules.rule)
        }

        // check if we have a header
        getHeader()?.let { header ->
            rules.addAll(clone(header).rules.rule)
        }

        if (options.enforceRuleLimit && rules.size > options.ruleLimit) {
            throw ItemFilterMergerException("Rule limit of ${options.ruleLimit} was exceeded.")
        }


        logger.info { "${rules.size} rules created in total." }

        mainFilter.rules = Rules(rules)
        var description = mainFilter.description?.trim() ?: ""
        if (description.isNotBlank()) {
            description += "\n"
        }
        description += "From ${Date()}."
        mainFilter.description = description
        return mainFilter
    }
}