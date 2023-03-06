package com.github.mrreallyyo.api.definitions


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
    val isFixRule: Boolean = (conditions?.condition?.size ?: 0) > 1 || !nameOverride.isNullOrBlank()
    val isAffixRule: Boolean =
        !isFixRule && conditions?.condition?.size == 1 && conditions!!.condition!!.first().isAffixCondition
    val isBaseRule: Boolean =
        !isFixRule && conditions?.condition?.size == 1 && conditions!!.condition!!.first().isBaseCondition

    fun recolor(color: Int) {
        this.color = color
        type = "HIGHLIGHT"
    }

    fun generateRuleName(fileName: String?) {
        val fileName = fileName ?: return

        val conditions = conditions?.condition ?: emptyList()

        val affixCondition = conditions.firstOrNull { it.isAffixCondition }
        val baseCondition = conditions.firstOrNull { it.isBaseCondition }

        nameOverride =
            if (nameOverride != null || conditions.size != 2 || affixCondition == null || baseCondition == null) {
                "$nameOverride from $fileName"
            } else {

                val base = (baseCondition.type?.equipmentType ?: listOf("UNKNOWN")).joinToString("|").replace("_", "")
                val baseTypes = baseCondition.subTypes?.int?.size ?: 0

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

                if(baseTypes > 0) {
                    name += "$baseTypes"
                } else {
                    name += "ANY"
                }
                name += " $base with $affixes AFFIXES ($howMany$match1$match2)"
                name
            }


    }


}