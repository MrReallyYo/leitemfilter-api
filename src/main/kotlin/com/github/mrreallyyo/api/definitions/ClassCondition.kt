package com.github.mrreallyyo.api.definitions

import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.annotation.JsonDeserialize

@JsonDeserialize(using = JsonDeserializer.None::class)
class ClassCondition : AbsCondition() {
    val req: String? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false

        other as ClassCondition

        if (req != other.req) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + (req?.hashCode() ?: 0)
        return result
    }

}