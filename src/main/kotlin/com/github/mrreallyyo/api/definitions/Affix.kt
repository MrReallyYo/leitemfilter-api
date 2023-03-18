package com.github.mrreallyyo.api.definitions

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue
import com.fasterxml.jackson.annotation.JsonValue

enum class Affix(
    @get:JsonValue
    val id: Int
) {

    ALL_ATTRIBUTES(id = 50),


    @JsonEnumDefaultValue
    UNKNOWN_AFFIX(id = -1337)

}