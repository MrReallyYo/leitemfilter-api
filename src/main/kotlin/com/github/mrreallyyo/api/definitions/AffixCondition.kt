package com.github.mrreallyyo.api.definitions

import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.annotation.JsonDeserialize


@JsonDeserialize(using = JsonDeserializer.None::class)
class AffixCondition : AbsCondition() {
    var affixes: Affixes = Affixes()
    var comparsion: Comparison = Comparison.ANY
    var comparsionValue: Int = 0
    var minOnTheSameItem: Int = 0
    var combinedComparsion: Comparison = Comparison.ANY
    var combinedComparsionValue: Int = 0
    var advanced: Boolean = false

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false

        other as AffixCondition

        if (affixes != other.affixes) return false
        if (comparsion != other.comparsion) return false
        if (comparsionValue != other.comparsionValue) return false
        if (minOnTheSameItem != other.minOnTheSameItem) return false
        if (combinedComparsion != other.combinedComparsion) return false
        if (combinedComparsionValue != other.combinedComparsionValue) return false
        if (advanced != other.advanced) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + (affixes.hashCode() ?: 0)
        result = 31 * result + (comparsion.hashCode() ?: 0)
        result = 31 * result + (comparsionValue ?: 0)
        result = 31 * result + (minOnTheSameItem ?: 0)
        result = 31 * result + (combinedComparsion.hashCode() ?: 0)
        result = 31 * result + (combinedComparsionValue ?: 0)
        result = 31 * result + (advanced.hashCode() ?: 0)
        return result
    }


}