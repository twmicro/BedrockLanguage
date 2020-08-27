package com.twmicro.bedrocklang

@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION,
    AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.EXPRESSION, AnnotationTarget.PROPERTY
)
@Retention(AnnotationRetention.SOURCE)
annotation class BedrockType ()