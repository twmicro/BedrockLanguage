package com.twmicro.bedrocklang

object BedrockLex {
    private const val CODE_BLOCK = "{}"
    private const val SEPERATOR = ","
    private const val CALL_FUNCTION = "()"
    private const val ASSIGNMENT = "="


    open class Construction (val content: String)
    class BedrockClass (val name: String, val constructorParams: Array<String>, val childrenCode: String) :
        Construction("$name${CALL_FUNCTION[0]}${constructorParams.joinToString(SEPERATOR)}${CALL_FUNCTION[1]}${CODE_BLOCK[0]}$childrenCode${CODE_BLOCK[1]}")
    class BedrockAssignment(val propertyName: String, val propertyValue: String):
        Construction(propertyName + ASSIGNMENT + propertyValue)
}