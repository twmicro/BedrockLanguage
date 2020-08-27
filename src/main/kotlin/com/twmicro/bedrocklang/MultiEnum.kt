package com.twmicro.bedrocklang

import kotlin.reflect.KClass


class MultiEnum<T : Enum<T>> (val clazz: KClass<T>, val bedrockValue: String) {
    fun toJVM() : T? {
        for(property in clazz.java.enumConstants){
            if(property.name.toLowerCase().contains(bedrockValue)) {
                return property
            }
        }
        return null
    }
}