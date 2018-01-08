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
}