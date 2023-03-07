package com.github.mrreallyyo.api.definitions


import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonInclude
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

    @JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = ConditionsFilter::class)
    var conditions: Conditions? = null
) {
    @JsonIgnore
    val isFixRule: Boolean = (conditions?.condition?.size ?: 0) > 1 || !nameOverride.isNullOrBlank()

    @JsonIgnore
    val isAffixRule: Boolean =
        !isFixRule && conditions?.condition?.size == 1 && conditions!!.condition!!.first().isAffixCondition

    @JsonIgnore
    val isSubTypeRule: Boolean =
        !isFixRule && conditions?.condition?.size == 1 && conditions!!.condition!!.first().isSubTypeCondition

    fun recolor(color: Int) {
        this.color = color
        type = "HIGHLIGHT"
    }

    fun generateRuleName(fileName: String?) {
        val fileName = fileName ?: return

        val conditions = conditions?.condition ?: emptyList()

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
                    "EQUAL" -> " TIER=${affixCondition.comparsionValue}"
                    "LESS" -> " TIER<${affixCondition.comparsionValue}"
                    "MORE" -> " TIER>${affixCondition.comparsionValue}"
                    "LESS_OR_EQUAL" -> " TIER<=${affixCondition.comparsionValue}"
                    "MORE_OR_EQUAL" -> " TIER>=${affixCondition.comparsionValue}"
                    else -> ""
                }

                val match2 = when (affixCondition.combinedComparsion) {
                    "EQUAL" -> " SUM=${affixCondition.combinedComparsionValue}"
                    "LESS" -> " SUM<${affixCondition.combinedComparsionValue}"
                    "MORE" -> " SUM>${affixCondition.combinedComparsionValue}"
                    "LESS_OR_EQUAL" -> " SUM<=${affixCondition.combinedComparsionValue}"
                    "MORE_OR_EQUAL" -> " SUM>=${affixCondition.combinedComparsionValue}"
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