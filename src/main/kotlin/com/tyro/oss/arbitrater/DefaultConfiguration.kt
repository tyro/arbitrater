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

import com.tyro.oss.randomdata.*
import java.math.BigDecimal
import java.math.BigInteger
import java.util.*

/**
 * This is the default configuration that will be applied to any new [InstanceCreator]s.
 *
 * Changing the settings on this class will affect the way any subsequent instances are generated.
 * This can be used to change the default generation settings and add support for new types. An alternative
 * approach is to implement [ArbitraterInitializer].
 */
object DefaultConfiguration : ConfigurableArbitrater() {
    private val random = Random()
    private var bootstrapped = false

    init {
        registerGenerator(RandomBoolean::randomBoolean)
        registerGenerator { random.nextInt(Byte.MAX_VALUE.toInt()).toByte() }
        registerGenerator { random.nextInt(Short.MAX_VALUE.toInt()).toShort() }
        registerGenerator(::randomByte)
        registerGenerator(random::nextInt)
        registerGenerator(random::nextLong)
        registerGenerator(random::nextFloat)
        registerGenerator(random::nextDouble)
        registerGenerator(RandomString::randomString)
        registerGenerator(::randomKotlinString)
        registerGenerator(RandomLocalDate::randomLocalDate)
        registerGenerator(RandomLocalDateTime::randomLocalDateTime)
        registerGenerator(RandomZonedDateTime::randomZonedDateTime)
        registerGenerator { RandomZonedDateTime.randomZonedDateTime().toInstant() }
        registerGenerator(UUID::randomUUID)
        registerGenerator { BigInteger.valueOf(random.nextLong()) }
        registerGenerator { BigDecimal.valueOf(random.nextDouble()) }

        bootstrap()
    }

    private fun bootstrap() {
        synchronized(this) {
            if (bootstrapped) {
                return
            }

            val bootstrapLoader = ServiceLoader.load(ArbitraterInitializer::class.java)
            bootstrapLoader
                    .sortedBy { it.priority }
                    .forEach { it.bootstrapConfiguration(this) }

            bootstrapped = true
        }
    }

    private fun randomByte(): Byte {
        val byteArray = ByteArray(1)
        random.nextBytes(byteArray)

        return byteArray[0]
    }

    private fun randomKotlinString(): String = RandomString.randomString() // Needed to convert the Java string to a Kotlin string
}