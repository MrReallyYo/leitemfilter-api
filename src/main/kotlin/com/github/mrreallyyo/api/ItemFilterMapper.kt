package com.github.mrreallyyo.api

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.dataformat.xml.XmlFactory
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.codehaus.stax2.XMLOutputFactory2
import javax.xml.stream.XMLInputFactory

object ItemFilterMapper {

    fun newMapper() : XmlMapper {
        val input = XMLInputFactory.newFactory()
        input.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, false)
        val output = XMLOutputFactory2.newFactory()
        output.setProperty(XMLOutputFactory2.P_AUTOMATIC_EMPTY_ELEMENTS, true)


        val mapper = XmlMapper.builder(XmlFactory(input, output)).apply {
            addModule(
                KotlinModule.Builder().apply {}.build()
            )
            configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            configure(SerializationFeature.INDENT_OUTPUT, true)
            configure(FromXmlParser.Feature.EMPTY_ELEMENT_AS_NULL, true)
        }.build()

        return mapper
    }

}