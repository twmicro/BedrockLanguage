package com.twmicro.bedrocklang

import kotlin.reflect.KClass
import kotlin.reflect.full.staticProperties

class MultiStatic<T : Any> (val clazz: KClass<T>, val bedrockValue: String){
    fun toJVM() : Any? {
        for(property in clazz.staticProperties){
            if(property.name.toLowerCase().contains(bedrockValue)) {
                return property.get()
            }
        }
        return null
    }
}