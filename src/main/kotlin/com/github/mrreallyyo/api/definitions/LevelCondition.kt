package com.github.mrreallyyo.api.definitions

import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.annotation.JsonDeserialize

@JsonDeserialize(using = JsonDeserializer.None::class)

class LevelCondition : AbsCondition() {
    var type: String? = null
    var treshold: Int? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false

        other as LevelCondition

        if (type != other.type) return false
        if (treshold != other.treshold) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + (type?.hashCode() ?: 0)
        result = 31 * result + (treshold ?: 0)
        return result
    }

}
