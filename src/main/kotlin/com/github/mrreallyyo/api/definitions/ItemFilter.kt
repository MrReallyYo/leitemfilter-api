package com.github.mrreallyyo.api.definitions

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.module.kotlin.readValue
import com.github.mrreallyyo.api.ItemFilterMapper
import mu.KotlinLogging
import java.io.InputStream
import java.io.OutputStream


data class ItemFilter(
    @JacksonXmlProperty(isAttribute = true, localName = "xmlns:i")
    var xmlns_i: String? = null,
    var name: String? = null,
    var filterIcon: Int? = null,
    var filterIconColor: Int? = null,
    var description: String? = null,
    var lastModifiedInVersion: String? = null,
    var lootFilterVersion: Int? = null,
    var rules: Rules? = null
) {
    @Transient
    var fileName: String? = null

    @Transient
    var ruleColorOverride: Int? = null


    companion object {
        private val logger = KotlinLogging.logger { }

       public fun load(stream: InputStream): ItemFilter {
            return ItemFilterMapper.newMapper().readValue(stream)
        }

    }

    fun write(stream: OutputStream) {
        ItemFilterMapper.newMapper().writeValue(stream, this)
    }


}