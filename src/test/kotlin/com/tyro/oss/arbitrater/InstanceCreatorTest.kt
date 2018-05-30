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

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNull
import kotlin.test.fail

class InstanceCreatorTest {

    @Test
    fun `can generate numbers`() {
        val numbers = Numbers::class.arbitraryInstance()
        println("numbers = $numbers")
    }

    @Test
    fun `can generate big numbers`() {
        val bigNumbers = BigNumbers::class.arbitraryInstance()
        println(bigNumbers)
    }

    @Test
    fun `can generate bools and bytes`() {
        val arbitraryInstance = BoolsAndBytes::class.arbitraryInstance()
        println("arbitraryInstance = $arbitraryInstance")
    }

    @Test
    fun `can generate strings`() {
        val strings = Strings::class.arbitraryInstance()
        println("strings = $strings")
    }

    @Test
    fun `can generate Java dates`() {
        val dates = Dates::class.arbitraryInstance()
        println(dates)
    }

    @Test
    fun `can generate UUIDs`() {
        val uuids = UUIDs::class.arbitraryInstance()
        println(uuids)
    }

    @Test
    fun `can generate kotlin enums`() {
        val kotlinNativeEnums = KotlinNativeEnums::class.arbitraryInstance()
        println(kotlinNativeEnums)
    }

    @Test
    fun `can generate Java enums`() {
        val javaEnums =  JavaEnums::class.arbitraryInstance()
        println(javaEnums)
    }

    @Test
    fun `can generate nested classes`() {
        val instance = NestedClasses::class.arbitraryInstance()
        println(instance)
    }

    @Test
    fun `can generate lists of values`() {
        val listOfValues = ListOfValues::class.arbitraryInstance()
        println("listOfValues = $listOfValues")
    }

    @Test
    fun `can generate lists of non-nested data classes`() {
        val instance = ListOfNonNestedDataClasses::class.arbitraryInstance()
        println(instance)
    }

    @Test
    fun `can generate lists of nested data classes`() {
        val instance = ListOfNestedDataClasses::class.arbitraryInstance()
        println(instance)
    }

    @Test
    fun `can generate sets`() {
        val instance = SetOfValues::class.arbitraryInstance()
        println(instance)
    }

    @Test
    fun `will supply lists if asked to generate a 'collection'`() {
        val instance = CollectionOfValues::class.arbitraryInstance()
        println(instance)
    }

    @Test
    fun `can generate maps`() {
        val instance = MapsOfDtos::class.arbitraryInstance()
        println(instance)
    }

    @Test
    fun `can apply specific values to named parameters`() {
        val specificValue = "Tyro"
        val instance = TestClass::class.arbitrater().withValue("property1", specificValue).createInstance()

        assertEquals(specificValue, instance.property1)
        assertNotEquals(specificValue, instance.property2)
    }

    @Test
    fun `can apply null values to named parameters`() {
        val specificValue: String? = null
        val instance = TestClass::class.arbitrater().withValue("property1", specificValue).createInstance()

        assertNull(instance.property1)
        assertNotEquals(specificValue, instance.property2)
    }

    @Test
    fun `should throw straight away if name is not a constructor param`() {
        val specificValue = "Tyro"
        try {
            NestingTestClass::class.arbitrater().withValue("property7", specificValue)
            fail("Should have thrown IAE")
        } catch (e: IllegalArgumentException) {
            // expected
        }
    }

    @Test
    fun `should not apply specific value to nested instances`() {
        val specificValue = "Tyro"
        val instance = NestingTestClass::class.arbitrater().withValue("property1", specificValue).createInstance()

        assertEquals(specificValue, instance.property1)
        assertNotEquals(specificValue, instance.nested.property1)
    }

    @Test
    fun `specific parameter values should not be propagated across InstanceCreator instances`() {
        val specificValue = "Tyro"
        val instance1 = TestClass::class.arbitrater().withValue("property1", specificValue).createInstance()
        val instance2 = TestClass::class.arbitrater().createInstance()

        assertEquals(specificValue, instance1.property1)
        assertNotEquals(specificValue, instance2.property1)
    }

    @Test
    fun `can use a specific parameter value that's not a property`() {
        val instance = ClassWithoutConstructorProperty::class.arbitrater().withValue("name", "Boy").createInstance()
        assertEquals("Big Boy", instance.bigName)
    }
}

class TestClass(val property1: String?, val property2: String)
class NestingTestClass(val nested: TestClass, val property1: String)
class ClassWithoutConstructorProperty(name: String) {
    val bigName = "Big " + name
}
