/*
 * Copyright [2018] Tyro Payments Limited.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tyro.oss.arbitrater

import com.tyro.oss.randomdata.RandomEnum
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.KType
import kotlin.reflect.KTypeProjection
import kotlin.reflect.full.createType
import kotlin.reflect.full.isSubtypeOf
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.withNullability

// TODO: Arrays?

private val wildcardMapType = Map::class.createType(arguments = listOf(KTypeProjection.STAR, KTypeProjection.STAR))
private val wildcardNullableMapType = Map::class.createType(arguments = listOf(KTypeProjection.STAR, KTypeProjection.STAR), nullable = true)
private val wildcardCollectionType = Collection::class.createType(arguments = listOf(KTypeProjection.STAR))
private val wildcardNullableCollectionType = Collection::class.createType(arguments = listOf(KTypeProjection.STAR), nullable = true)
private val wildcardListType = List::class.createType(arguments = listOf(KTypeProjection.STAR))
private val wildcardNullableListType = List::class.createType(arguments = listOf(KTypeProjection.STAR), nullable = true)
private val wildcardSetType = Set::class.createType(arguments = listOf(KTypeProjection.STAR))
private val wildcardNullableSetType = Set::class.createType(arguments = listOf(KTypeProjection.STAR), nullable = true)
private val wildcardEnumType = Enum::class.createType(arguments = listOf(KTypeProjection.STAR))
private val wildcardNullableEnumType = Enum::class.createType(arguments = listOf(KTypeProjection.STAR), nullable = true)

class InstanceCreator<out T : Any>(private val targetClass: KClass<T>, settings: GeneratorSettings = GeneratorSettings())
    : ConfigurableArbitrater(settings, DefaultConfiguration.generators.toMutableMap()) {

    private val specificValues: MutableMap<KParameter, Any?> = mutableMapOf()

    fun generateNulls(value: Boolean = true): InstanceCreator<T> = InstanceCreator(targetClass, settings.copy(generateNulls = value))

    fun useDefaultValues(value: Boolean = false): InstanceCreator<T> = InstanceCreator(targetClass, settings.copy(useDefaultValues = value))

    fun withValue(parameterName: String, value: Any?): InstanceCreator<T> {
        targetClass.primaryConstructor?.parameters?.find { it.name == parameterName }?.let {
            specificValues[it] = value
        } ?: throw IllegalArgumentException("Parameter named $parameterName not found in primary constructor of ${targetClass.simpleName}")

        return this
    }

    /**
     * Create an arbitrary instance, or else explode
     */
    fun createInstance(): T {
        validateConstructor()

        try {
            val primaryConstructor = targetClass.primaryConstructor!!

            val constructorArguments = primaryConstructor
                    .parameters
                    .filterNot { it.isOptional && settings.useDefaultValues }
                    .map { it to (if(specificValues.containsKey(it)) specificValues[it] else it.type.randomValue()) }
                    .toMap()

            return primaryConstructor.callBy(constructorArguments)
        } catch (e: Exception) {
            throw RuntimeException("Could not generate random value for class [${targetClass.qualifiedName}]", e)
        }
    }

    private fun validateConstructor() {
        requireNotNull(targetClass.primaryConstructor) {
            """
            Target class [${targetClass.qualifiedName}] has no primary constructor. This is probably a Java class. Call 'registerGenerator' to supply an instance generator.
            """
        }
    }

    private fun KType.randomValue(): Any? {
        val nonNullableType = withNullability(false)

        return when {
            settings.generateNulls && isMarkedNullable -> null
            canGenerate(nonNullableType) -> generate(withNullability(false))
            isSubtypeOf(wildcardCollectionType) -> fillCollection(this)
            isSubtypeOf(wildcardNullableCollectionType) -> fillCollection(this)
            isSubtypeOf(wildcardMapType) -> fillMap(this)
            isSubtypeOf(wildcardNullableMapType) -> fillMap(this)
            isSubtypeOf(wildcardEnumType) -> RandomEnum.randomEnumValue((classifier as KClass<Enum<*>>).java)
            isSubtypeOf(wildcardNullableEnumType) -> RandomEnum.randomEnumValue((classifier as KClass<Enum<*>>).java)
            classifier is KClass<*> -> InstanceCreator(classifier as KClass<*>, settings).createInstance()
            else -> TODO("No support for ${this}")
        }
    }

    private fun fillMap(mapType: KType): Any {
        val keyType = mapType.arguments[0].type!!
        val valueType = mapType.arguments[1].type!!

        return (1..10)
                .map { keyType.randomValue() to valueType.randomValue() }
                .toMap()
    }

    private fun fillCollection(collectionType: KType): Any {
        val valueType = collectionType.arguments[0].type!!
        val randomValues = (1..10).map { valueType.randomValue() }

        return when {
            collectionType.isSubtypeOf(wildcardListType) -> randomValues
            collectionType.isSubtypeOf(wildcardNullableListType) -> randomValues
            collectionType.isSubtypeOf(wildcardSetType) -> randomValues.toSet()
            collectionType.isSubtypeOf(wildcardNullableSetType) -> randomValues.toSet()
            collectionType.isSubtypeOf(wildcardCollectionType) -> randomValues
            collectionType.isSubtypeOf(wildcardNullableCollectionType) -> randomValues
            else -> TODO("No support for $collectionType")
        }
    }

    private fun generate(type: KType): Any = generators[type]!!.invoke()

    private fun canGenerate(type: KType) = generators.containsKey(type)
}

data class GeneratorSettings(
        val useDefaultValues: Boolean = true,
        val generateNulls: Boolean = false
)

