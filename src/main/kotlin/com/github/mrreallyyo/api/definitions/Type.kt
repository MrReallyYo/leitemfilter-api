package com.github.mrreallyyo.api.definitions

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement

@JacksonXmlRootElement(localName = "type")
data class Type(
    @JacksonXmlProperty(localName = "EquipmentType")
    @JacksonXmlElementWrapper(useWrapping = false)
    var equipmentType: List<String>? = null
)