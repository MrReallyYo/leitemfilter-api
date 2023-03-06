package com.github.mrreallyyo.api.definitions

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty

data class Rules(
    @JacksonXmlProperty(localName = "Rule")
    @JacksonXmlElementWrapper(useWrapping = false)
    var rule: List<Rule>? = null
)