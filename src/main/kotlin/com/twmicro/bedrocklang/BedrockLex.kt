package com.twmicro.bedrocklang

import java.lang.reflect.ParameterizedType
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.*
import kotlin.reflect.jvm.javaType

object BedrockLex {
    private const val CODE_BLOCK = "{}"
    private const val SEPERATOR = ","
    private const val CALL_FUNCTION = "()"
    private const val ASSIGNMENT = "="

    fun parse(source: String) {
        val buffer: String = ""
        val baseClassRegex = Regex("""\{.*}""", RegexOption.DOT_MATCHES_ALL)
        val result = baseClassRegex.find(source)
        try {
            if (result != null) {
                val childrenCode = result.value.replace("{", "").replace("}", "")
                val baseClassName = source.substring(0, source.indexOf("(") - 1).replace(" ", "")
                val args = source.substring(source.indexOf("(") + 1, source.indexOf(")")).replace(" ", "").split(SEPERATOR).toTypedArray()
                val bedrockClass = BedrockClass(baseClassName, args, childrenCode)
                execute(bedrockClass)

            }
            else throw BedrockException("Missing body!")
        }
        catch(e: Exception) {
            throw BedrockException(e.message)
        }
    }

    fun execute(baseClass: BedrockClass) {
        BedrockLang::class.nestedClasses.forEach { it ->
            if(it.hasAnnotation<BedrockType>()) {
                if(it.simpleName != null && it.simpleName?.contains(baseClass.name)!!){
                    if(it.primaryConstructor != null) {
                        val instance = it.primaryConstructor!!.call(baseClass.constructorParams[0])
                        val assignments = baseClass.childrenCode.split(SEPERATOR)
                        it.memberProperties.forEach { it1 ->
                            if(it1.findAnnotation<BedrockType>() != null) {
                                if(it1 is KMutableProperty<*>) {
                                    for(a in assignments){
                                        if(a.contains(it1.name)) {
                                            if (it1.returnType.classifier == String::class)
                                                it1.setter.call(instance, a.split(ASSIGNMENT)[1].replace(" ", "").replace("\n", ""))
                                            else if(it1.returnType.classifier == Float::class)
                                                it1.setter.call(instance, a.split(ASSIGNMENT)[1].replace(" ", "").replace("\n", "").toFloat())
                                            else if(it1.returnType.classifier == MultiStatic::class)
                                                it1.setter.call(instance, MultiStatic(it1.returnType.arguments[0].type!!.classifier as KClass<*>, a.split(ASSIGNMENT)[1].replace(" ", "").replace("\n", "")))
                                        }
                                    }
                                }
                            }
                        }
                        instance.javaClass.getDeclaredMethod("execute").invoke(instance)
                    }
                }
            }
        }
    }

    open class Construction (val content: String)
    class BedrockClass (val name: String, val constructorParams: Array<String>, val childrenCode: String) :
        Construction("$name${CALL_FUNCTION[0]}${constructorParams.joinToString(SEPERATOR)}${CALL_FUNCTION[1]}${CODE_BLOCK[0]}$childrenCode${CODE_BLOCK[1]}")
    class BedrockAssignment(val propertyName: String, val propertyValue: String):
        Construction(propertyName + ASSIGNMENT + propertyValue)
}