package org.buldakov.huawei.modem.xml

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.core.JsonToken
import com.fasterxml.jackson.databind.BeanDescription
import com.fasterxml.jackson.databind.BeanProperty
import com.fasterxml.jackson.databind.DeserializationConfig
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier
import com.fasterxml.jackson.databind.deser.std.CollectionDeserializer
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.type.CollectionType
import java.io.IOException

class XmlWhitespaceModule : SimpleModule() {
    private class CustomizedCollectionDeserializer(src: CollectionDeserializer?) :
        CollectionDeserializer(src) {

        @Suppress("UNCHECKED_CAST")
        @Throws(IOException::class, JsonProcessingException::class)
        override fun deserialize(jp: JsonParser, ctxt: DeserializationContext?): Collection<Any> {
            return if (jp.currentToken == JsonToken.VALUE_STRING && jp.text.isBlank()) {
                _valueInstantiator.createUsingDefault(ctxt) as Collection<Any>
            } else {
                super.deserialize(jp, ctxt)
            }
        }

        @Throws(JsonMappingException::class)
        override fun createContextual(
            ctxt: DeserializationContext?,
            property: BeanProperty?
        ): CollectionDeserializer {
            return CustomizedCollectionDeserializer(super.createContextual(ctxt, property))
        }

        companion object {
            private const val serialVersionUID = 1L
        }
    }

    override fun setupModule(context: SetupContext) {
        super.setupModule(context)
        context.addBeanDeserializerModifier(object : BeanDeserializerModifier() {
            override fun modifyCollectionDeserializer(
                config: DeserializationConfig?, type: CollectionType?,
                beanDesc: BeanDescription?, deserializer: JsonDeserializer<*>?
            ): JsonDeserializer<*> {
                return if (deserializer is CollectionDeserializer) {
                    CustomizedCollectionDeserializer(
                        deserializer as CollectionDeserializer?
                    )
                } else {
                    super.modifyCollectionDeserializer(
                        config, type, beanDesc,
                        deserializer
                    )
                }
            }
        })
    }

    companion object {
        private const val serialVersionUID = 1L
    }
}