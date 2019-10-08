# Arbitrater

[![Download](https://maven-badges.herokuapp.com/maven-central/com.tyro.oss/arbitrater/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.tyro.oss/arbitrater)
[![Build Status](https://travis-ci.org/tyro/arbitrater.svg?branch=master)](https://travis-ci.org/tyro/arbitrater)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0)

Arbitrater is a library for creating arbitrary instances of classes by reflection,
for example to use in testing. It was designed primarily for use with Kotlin 'data classes', 
but may work with regular classes too.

It is similar to libraries like [random-beans](https://github.com/benas/random-beans) and
[podam](https://devopsfolks.github.io/podam/), but with support for nullable types and default values. 

## Quick start

```kotlin
    data class Order(val name: String, val quantity: Int = 1, val promotionCode: String?)
    
       
    val defaultOrder = Order::class.arbitraryInstance()
    println(defaultOrder) // Order(description=\VccV/esz{54[!FAU(a{, quantity=1, promotionCode=5b$`i1AsT54t[Hwf%W*&)    
    
    val customOrder = Order::class.arbitrater()
            .generateNulls()
            .useDefaultValues(false)
            .createInstance()    
            
    println(customOrder) // Order(description= 5{L_vUnvMIJ!y]03*<c, quantity=513355083, promotionCode=null) 
```

## Features

### Default generators

Out of the box, Arbitrater can generate values for:
* Byte, Short, Int, Long
* Double, Float
* BigInteger, BigDecimal
* String
* UUID
* LocalDate, LocalDateTime
* Enums
* Lists of any supported type
* Sets of any supported type
* Collections of any supported type (generates a non-mutable list)
* Maps of any supported type
* Classes with a primary constructor that consist of any supported type
* Nested classes that meet the above requirements. 

### Customising and extending

The options below can be applied either per-instance, or globally.

To customise per-instance, call '`arbitrater()`', which provides a fluent API for configuration.

To customise globally, either use the `DefaultConfiguration` object or provide a bootstrap configuration. (See the next section) 

#### Customising generated instances

Currently the following properties can be configured:
* Whether nullable types have values generated for them, or are left as null
* Whether optional parameters have values generated for them, or are left as the default value. 

#### Extending supported types

If you need to generate a type that does not work out of the box, or you want to customise the way a type is generated, 
call `registerGenerator`. It takes a lambda or method reference with no parameters that returns a single value.

If multiple generators are registered for the same type, the last one wins. 

Note: If the return type of the function is nullable, it is projected as a non-nullable type. 
This is to ease interoperability with Java.

 
### Bootstrap configuration

It is possible to create a bootstrap configuration file that will configure Arbitrater for your library / project automatically. This feature makes use of the `ServiceLoader` mechanism provided by core Java.

1. Implement the interface `ArbitraterInitializer`
   * Set a priority. If there are multiple bootstrap configurations with overlapping settings, the highest priority wins.
   * In the method `bootstrapConfiguration()`, register any generators you require. You can also change the default settings.
2. Create the directory META-INF/services
3. Add a file called 'com.tyro.oss.arbitrater.ArbitraterInitializer' to this directory 
4. Add a single line to the file with the 'binary name' of your implementing class. In many cases this will be the fully qualified class name.
5. That's it - your code will be executed once, the first time an arbitrary instance is generated.  

Note: You can replace steps 2-5 with the [AutoService](https://github.com/google/auto/tree/master/service) library

### Limitations

* Currently only classes with a primary constructor are supported - this means 'POJOS' defined in Java libraries may not work
* Arrays are not supported. This is because the primary use case was data-classes, and arrays do not play nicely with data classes.
* Error handling is not as informative as it could be 