package com.github.mrreallyyo.api.definitions

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper

data class SubTypes(
    @JacksonXmlElementWrapper(useWrapping = false, localName = "int")
    var int: List<String>? = null
)