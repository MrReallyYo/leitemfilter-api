package com.github.mrreallyyo.api.definitions

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.module.kotlin.readValue
import com.github.mrreallyyo.api.ItemFilterMapper
import java.io.InputStream
import java.io.OutputStream

data class ItemFilter(
    @JacksonXmlProperty(isAttribute = true, localName = "xmlns:i")
    var xmlns_i: String,
    var name: String? = null,
    var filterIcon: Int = 0,
    var filterIconColor: Int = 0,
    var description: String? = null,
    var lastModifiedInVersion: String,
    var lootFilterVersion: Int = 0,
    @JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = RulesFilter::class)
    var rules: Rules = Rules()
) {
    @JsonIgnore
    var fileName: String? = null

    companion object {
       fun load(stream: InputStream): ItemFilter {
            return ItemFilterMapper.newMapper().readValue(stream)
        }

    }

    fun write(stream: OutputStream) {
        ItemFilterMapper.newMapper().writeValue(stream, this)
    }


}