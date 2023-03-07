package com.github.mrreallyyo.api.definitions

import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.annotation.JsonDeserialize

@JsonDeserialize(using = JsonDeserializer.None::class)
class SubTypeCondition : AbsCondition() {
    var type: Type? = null
    var subTypes: SubTypes? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false

        other as SubTypeCondition

        if (type != other.type) return false
        if (subTypes != other.subTypes) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + (type?.hashCode() ?: 0)
        result = 31 * result + (subTypes?.hashCode() ?: 0)
        return result
    }

}