package com.github.mrreallyyo.api.definitions


import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement

@JacksonXmlRootElement(localName = "Rule")
data class Rule(
    var type: String,
    var color: Int,
    var isEnabled: Boolean,
    var levelDependent: Boolean,
    var minLvl: Int,
    var maxLvl: Int,
    var emphasized: Boolean,
    var nameOverride: String?,

    @JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = ConditionsFilter::class)
    var conditions: Conditions = Conditions()
) {

    @JsonIgnore
    var isMainFilter: Boolean = false

    @JsonIgnore
    val isFixRule: Boolean = conditions.condition.size > 1 || !nameOverride.isNullOrBlank()

    @JsonIgnore
    val isAffixRule: Boolean =
        !isFixRule && conditions.condition.size == 1 && conditions.condition.first().isAffixCondition

    @JsonIgnore
    val isSubTypeRule: Boolean =
        !isFixRule && conditions.condition.size == 1 && conditions.condition.first().isSubTypeCondition

    fun recolor(color: Int) {
        this.color = color
        type = "HIGHLIGHT"
    }

    fun generateRuleName(fileName: String?) {
        val fileName = fileName ?: return

        val conditions = conditions.condition ?: emptyList()

        val affixCondition = conditions.firstOrNull { it.isAffixCondition } as? AffixCondition
        val subTypeCondition = conditions.firstOrNull { it.isSubTypeCondition } as? SubTypeCondition

        nameOverride =
            if (nameOverride != null || conditions.size != 2 || affixCondition == null || subTypeCondition == null) {
                "from $fileName\n$nameOverride"
            } else {

                val subType =
                    (subTypeCondition.type?.equipmentType ?: listOf("UNKNOWN")).joinToString("|").replace("_", "")
                val subTypeCount = subTypeCondition.subTypes?.int?.size ?: 0

                val affixes = affixCondition.affixes?.int?.size ?: 0


                val howMany = "NUM>=${affixCondition.minOnTheSameItem}"

                val match1 = when (affixCondition.comparsion) {
                    Comparison.EQUAL -> " TIER=${affixCondition.comparsionValue}"
                    Comparison.LESS -> " TIER<${affixCondition.comparsionValue}"
                    Comparison.MORE -> " TIER>${affixCondition.comparsionValue}"
                    Comparison.LESS_OR_EQUAL -> " TIER<=${affixCondition.comparsionValue}"
                    Comparison.MORE_OR_EQUAL -> " TIER>=${affixCondition.comparsionValue}"
                    else -> ""
                }

                val match2 = when (affixCondition.combinedComparsion) {
                    Comparison.EQUAL -> " SUM=${affixCondition.combinedComparsionValue}"
                    Comparison.LESS -> " SUM<${affixCondition.combinedComparsionValue}"
                    Comparison.MORE -> " SUM>${affixCondition.combinedComparsionValue}"
                    Comparison.LESS_OR_EQUAL -> " SUM<=${affixCondition.combinedComparsionValue}"
                    Comparison.MORE_OR_EQUAL -> " SUM>=${affixCondition.combinedComparsionValue}"
                    else -> ""
                }

                var name = "from $fileName\n"

                name += if (subTypeCount > 0) {
                    "$subTypeCount"
                } else {
                    "ANY"
                }
                name += " $subType with $affixes AFFIXES ($howMany$match1$match2)"
                name
            }

    }

}