package com.github.mrreallyyo.api.definitions

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement

@JacksonXmlRootElement(localName = "type")
data class Type(
    @JsonFormat(with = [JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY])
    @JacksonXmlProperty(localName = "EquipmentType")
    @JacksonXmlElementWrapper(useWrapping = false)
    var equipmentType: List<String>? = null
) {


}