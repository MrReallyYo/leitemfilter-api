package com.github.mrreallyyo.api

import com.github.mrreallyyo.api.definitions.*
import mu.KotlinLogging
import java.util.*

object ItemFilterMerger {

    private val logger = KotlinLogging.logger { }

    fun mergeFilter(
        compactFilters: List<ItemFilter>, colors: List<Int>
    ): ItemFilter {

        logger.info { "Using embedded filter header." }
        val header = ItemFilterMerger::class.java.classLoader.getResourceAsStream("_header.xml")!!.use {
            ItemFilter.load(it)
        }
        logger.info { "Using embedded filter footer." }
        val footer = ItemFilterMerger::class.java.classLoader.getResourceAsStream("_footer.xml")!!.use {
            ItemFilter.load(it)
        }
        return mergeFilter(header, footer, compactFilters, colors)
    }

    fun mergeFilter(
        header: ItemFilter, footer: ItemFilter, compactFilters: List<ItemFilter>, colors: List<Int>
    ): ItemFilter {

        val filter = clone(compactFilters.first())

        val rules = mutableListOf<Rule>()
        // footer rules
        rules.addAll(clone(footer).rules!!.rule!!)

        val colorsForOverride = mutableListOf<Int>()
        if (colors.size >= compactFilters.size - 1) {
            // we can match a color for each filter
            logger.info { "Got individual override colors for each secondary filter." }
            for (i in 0 until compactFilters.size - 1) {
                colorsForOverride.add(colors[i])
            }
        } else if (colors.isNotEmpty()) {
            // only use first color
            logger.info { "Got override color for all secondary filter." }
            for (i in 0 until compactFilters.size - 1) {
                colorsForOverride.add(colors.first())
            }
        }

        compactFilters.reversed().forEach { compactFilter ->
            logger.info { "Parsing ${compactFilter.fileName}." }

            val fixRules = mutableListOf<Rule>()
            val affixRules = mutableListOf<Rule>()
            val baseRules = mutableListOf<Rule>()
            // collect rules by type
            (compactFilter.rules?.rule ?: emptyList()).forEach { rule ->
                when {
                    rule.isAffixRule -> if (rule.isEnabled == true) affixRules.add(rule)
                    rule.isBaseRule -> if (rule.isEnabled == true) baseRules.add(rule)
                    rule.isFixRule -> fixRules.add(rule)
                }
            }

            logger.info { "Found ${fixRules.size} fix rules." }
            logger.info { "Found ${affixRules.size} affix rules." }
            logger.info { "Found ${baseRules.size} base rules." }
            logger.info { "Creating ${affixRules.size * baseRules.size} filter rules." }


            val overrideColor = colorsForOverride.removeLastOrNull()

            // build combined affix/base rules
            affixRules.forEach { affix ->
                baseRules.forEach { base ->
                    val conditions = mutableListOf<Condition>()
                    affix.conditions?.condition?.firstOrNull()?.let { conditions.add(it) }
                    base.conditions?.condition?.firstOrNull()?.let { conditions.add(it) }

                    val combined = clone(affix)
                    combined.conditions = Conditions(conditions)

                    if (overrideColor != null) {
                        combined.recolor(overrideColor)
                    }
                    combined.generateRuleName(compactFilter.fileName)
                    rules.add(combined)
                }
            }

            fixRules.forEach {
                if (it.nameOverride.isNullOrBlank()) {
                    it.nameOverride = "Generated from ${compactFilter.fileName}"
                } else {
                    it.nameOverride += " from ${compactFilter.fileName}"
                }
                if (overrideColor != null) {
                    it.recolor(overrideColor)
                }
            }

            if (overrideColor != null) {
                fixRules.forEach { it.recolor(overrideColor) }
            }

            // add the fixed rules
            rules.addAll(fixRules)
        }


        // header rules
        rules.addAll(clone(header).rules!!.rule!!)

        logger.info { "${rules.size} rules created in total." }


        filter.rules = Rules(rules)
        var description = filter.description?.trim() ?: ""
        if (description.isNotBlank()) {
            description += "\n"
        }
        description += "From ${Date()}."
        filter.description = description
        return filter
    }
}