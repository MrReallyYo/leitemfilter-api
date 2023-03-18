package com.github.mrreallyyo.api

import com.github.mrreallyyo.api.definitions.AffixCondition
import com.github.mrreallyyo.api.definitions.Comparison
import com.github.mrreallyyo.api.definitions.Rule

class RuleComparator : Comparator<Rule> {
    override fun compare(rule1: Rule, rule2: Rule): Int {
        if (!rule1.isAffixRule && !rule2.isAffixRule) {
            return rule1.conditions.condition.size - rule2.conditions.condition.size
        }
        if (rule1.isAffixRule && !rule2.isAffixRule) return 1
        if (!rule1.isAffixRule && rule2.isAffixRule) return -1

        val affixCondition1 = rule1.conditions.condition.first { it.isAffixCondition } as AffixCondition
        val affixCondition2 = rule2.conditions.condition.first { it.isAffixCondition } as AffixCondition

        val affixesRequiredCompare = affixCondition1.minOnTheSameItem - affixCondition2.minOnTheSameItem
        if (affixesRequiredCompare != 0) return affixesRequiredCompare

        val possibleMatchesOnSameItemResult =
            possibleMatchesOnSameItem(affixCondition1) - possibleMatchesOnSameItem(affixCondition2)
        if (possibleMatchesOnSameItemResult != 0) return possibleMatchesOnSameItemResult * -1

        val possibleMatchesForAffixTierResult =
            possibleMatchesForAffixTier(affixCondition1) - possibleMatchesForAffixTier(affixCondition2)
        if (possibleMatchesForAffixTierResult != 0) return possibleMatchesForAffixTierResult * -1


        val affixCountResult = affixCondition1.affixes.int.size - affixCondition2.affixes.int.size
        if (affixCountResult != 0) return affixCountResult * -1

        return if (rule1.isMainFilter && !rule2.isMainFilter) {
            1
        } else if (!rule1.isMainFilter && rule2.isMainFilter) {
            -1
        } else {
            0
        }
    }

    private val maxAffixPerItem = 4
    private val minAffixPerItem = 0
    private val maxTierPerAffix = 7
    private val minTierPerAffix = 0
    private val maxTier = maxAffixPerItem * maxTierPerAffix
    private val minTier = minAffixPerItem * minTierPerAffix

    private fun possibleMatchesOnSameItem(affixCondition: AffixCondition): Int {
        val limitedCombinedValue = affixCondition.combinedComparsionValue.coerceAtMost(maxTier).coerceAtLeast(minTier)
        return when (affixCondition.combinedComparsion) {
            Comparison.EQUAL -> 1
            Comparison.MORE -> maxTier - limitedCombinedValue
            Comparison.MORE_OR_EQUAL -> maxTier - limitedCombinedValue + 1
            Comparison.LESS -> limitedCombinedValue - 1 - minTierPerAffix
            Comparison.LESS_OR_EQUAL -> limitedCombinedValue - minTierPerAffix
            else -> maxTier
        }
    }

    private fun possibleMatchesForAffixTier(affixCondition: AffixCondition): Int {
        val limitedTierValue =
            affixCondition.comparsionValue.coerceAtMost(maxTierPerAffix).coerceAtLeast(minTierPerAffix)
        return when (affixCondition.comparsion) {
            Comparison.EQUAL -> 1
            Comparison.MORE -> maxTierPerAffix - limitedTierValue
            Comparison.MORE_OR_EQUAL -> maxTierPerAffix - limitedTierValue + 1
            Comparison.LESS -> limitedTierValue - 1
            Comparison.LESS_OR_EQUAL -> limitedTierValue
            else -> maxTierPerAffix
        }
    }


}