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

import java.math.BigDecimal
import java.math.BigInteger
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.util.*
import java.util.concurrent.TimeUnit

data class MapsOfDtos(val map: Map<String, NestedClasses>)

data class ListOfNonNestedDataClasses(val dtoList: List<Numbers>)

data class ListOfNestedDataClasses(val dtoList: List<NestedClasses>)

data class ListOfValues(val intList: List<Int>)

data class SetOfValues(val intList: Set<Int>)

data class CollectionOfValues(val intCollection: Collection<Int>)

data class DefaultValue(val int: Int = 10)

data class NullableValue(val date: LocalDate?)

data class NestedTypesWithNullableAndDefaultValues(val defaultValue: DefaultValue, val nullableValue: NullableValue)

data class NullableAndDefaultValues(val date: LocalDate? = null, val int: Int = 10)

data class Dates(
        val localDate: LocalDate,
        val localDateTime: LocalDateTime,
        val zonedDateTime: ZonedDateTime
)

data class BigNumbers(
        val bigInteger: BigInteger,
        val bigDecimal: BigDecimal
)

data class Numbers(
        val byte: Byte,
        val short: Short,
        val int: Int,
        val long: Long,
        val float: Float,
        val double: Double
)

data class BoolsAndBytes(val bool: Boolean, val byte: Byte)

data class Strings(val string1: String, val string2: String)

data class NestedClasses(val numbers: Numbers, val boolsAndBytes: BoolsAndBytes, val strings: Strings)

data class UUIDs(val uuid: UUID)

data class KotlinNativeEnums(val someEnum: SomeEnum)

data class JavaEnums(val someJavaEnum: TimeUnit)

enum class SomeEnum {
    VALUE_1,
    VALUE_2
}