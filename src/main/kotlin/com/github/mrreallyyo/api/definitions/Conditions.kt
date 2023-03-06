package com.github.mrreallyyo.api.definitions

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty

data class Conditions(
    @JacksonXmlProperty(localName = "Condition")
    @JacksonXmlElementWrapper(useWrapping = false)
    var condition: List<Condition>? = null
)