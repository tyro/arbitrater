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

import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.collections.shouldNotContain
import io.kotest.matchers.collections.shouldNotContainNoNulls
import io.kotest.matchers.collections.shouldNotContainNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.Test

class ArbitraterApiTest {

    @Test
    fun `arbitrary instance`() {
        arbitrary<DefaultValue>().int shouldNotBe null
    }

    @Test
    fun `nullable types generate values by default`() {
        val arbitraryInstance = NullableValue::class.arbitraryInstance()
        arbitraryInstance.date shouldNotBe null
    }

    @Test
    fun `can generate nulls for null values if desired`() {
        val arbitraryInstance = NullableValue::class.arbitrater()
                .generateNulls()
                .createInstance()

        arbitraryInstance.date shouldBe null
    }

    @Test
    fun `uses default values by default`() {
        val arbitraryInstances = (1..100).map {
            DefaultValue::class.arbitraryInstance()
        }

        arbitraryInstances.map { it.int }.shouldContainAll(10)
    }

    @Test
    fun `can elect not to use default values`() {
        val arbitraryInstances = (1..100).map {
            DefaultValue::class.arbitrater()
                    .useDefaultValues(false)
                    .createInstance()
        }

        arbitraryInstances shouldNotContain DefaultValue(10)
    }

    @Test
    fun `null and default type options propagate when generating nested classes`() {
        val instances = (1..10).map {
            NestedTypesWithNullableAndDefaultValues::class.arbitrater()
                    .generateNulls(true)
                    .useDefaultValues(false)
                    .createInstance()
        }

        instances.map { it.defaultValue.int } shouldNotContain 10
        instances.map { it.nullableValue.date }.shouldNotContainNoNulls()
    }

    @Test
    fun `convenience method 'WithAllPropertiesRandomized' will not generate nulls and generates values for default types`() {
        val arbitraryInstances = (1..100).map {
            NullableAndDefaultValues::class.arbitraryInstanceWithAllPropertiesRandomized()
        }

        arbitraryInstances.map { it.int } shouldNotContain 10
        arbitraryInstances.map { it.date }.shouldNotContainNull()
    }
}