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

/**
 * Interface implemented by classes participating in bootstrapping Arbitrater.
 *
 * Instances of this interface are discovered using a [java.util.ServiceLoader] the first time Arbitrater is invoked.
 */
interface ArbitraterInitializer {

    /**
     * This method will be called by Arbitrater once in the lifecyle of the JVM when arbitrater is invoked for the first time.
     *
     * Register any custom generators here. You can also override existing generator and change the default generation settings.
     *
     * @param configuration A configuration object that will be used to provide defaults to the Arbitrater library
     */
    fun bootstrapConfiguration(configuration: ConfigurableArbitrater)

    /**
     * Affects the order in which initializers will be applied. Initializers with a lower priority will be executed first. If multiple initializers modify the settings or register the same type,
     * the last one wins. Therefore, initializers with a higher priority will take precedence.
     */
    val priority: Int
}