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

import kotlin.reflect.KType
import kotlin.reflect.full.withNullability
import kotlin.reflect.jvm.reflect

abstract class ConfigurableArbitrater(protected var settings: GeneratorSettings = GeneratorSettings(), generators: MutableMap<KType, () -> Any>  = mutableMapOf()) {
    private val _generators: MutableMap<KType, () -> Any> = generators

    /**
     * The map of functions that will be used to generate random values
     */
    val generators: Map<KType, () -> Any>
        get() = _generators.toMap()

    /**
     * Register a custom type generator. Only one generator can be registered for a given type. Registering a type again will replace the previous generator.
     */
    fun registerGenerator(generator: () -> Any) {
        // Removing nullability so generators registered by passing in Java methods (with a platform type) will match up against a non-nullable Kotlin parameter declaration
        val returnType = generator.reflect()!!.returnType.withNullability(false)
        _generators[returnType] = generator
    }
}