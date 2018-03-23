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

import io.kotlintest.forAll
import io.kotlintest.forAtLeast
import io.kotlintest.matchers.shouldBe
import io.kotlintest.matchers.shouldNotBe
import org.junit.Test

class ArbitraterApiTest {

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

        forAll(arbitraryInstances) { instance ->
            instance.int shouldBe 10
        }
    }

    @Test
    fun `can elect not to use default values`() {
        val arbitraryInstances = (1..100).map {
            DefaultValue::class.arbitrater()
                    .useDefaultValues(false)
                    .createInstance()
        }

        forAtLeast(1, arbitraryInstances) { instance ->
            instance.int shouldNotBe 10
        }
    }

    @Test
    fun `null and default type options propagate when generating nested classes`() {
        val instances = (1..10).map {
            NestedTypesWithNullableAndDefaultValues::class.arbitrater()
                    .generateNulls(true)
                    .useDefaultValues(false)
                    .createInstance()
        }

        forAtLeast(1, instances) {
            it.defaultValue.int shouldNotBe 10
        }

        forAll(instances) {
            it.nullableValue.date shouldBe null
        }
    }

    @Test
    fun `convenience method 'WithAllPropertiesRandomized' will not generate nulls and generates values for default types`() {
        val arbitraryInstances = (1..100).map {
            NullableAndDefaultValues::class.arbitraryInstanceWithAllPropertiesRandomized()
        }

        forAtLeast(1, arbitraryInstances) { instance ->
            instance.int shouldNotBe 10
        }

        forAll(arbitraryInstances) { instance ->
            instance.date shouldNotBe null
        }
    }
}