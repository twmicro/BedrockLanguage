package com.twmicro.bedrocklang

import javax.annotation.processing.SupportedAnnotationTypes

@Target(AnnotationTarget.TYPE, AnnotationTarget.CLASS, AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
@SupportedAnnotationTypes
annotation class BedrockType