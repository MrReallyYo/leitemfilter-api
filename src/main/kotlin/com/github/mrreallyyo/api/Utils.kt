package com.github.mrreallyyo.api

inline fun <reified T> clone(what: T): T {
    val mapper = ItemFilterMapper.newMapper()
    val store = mapper.writeValueAsString(what)
    return mapper.readValue(store, T::class.java)
}