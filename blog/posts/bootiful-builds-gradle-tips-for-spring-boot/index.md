---
title: Bootiful Builds — Gradle Tips for Spring Boot Projects
summary: A look at the latest features and general best practices for building "bootiful" Spring Boot projects with Gradle.
categories: [ Gradle ]
tags: [ gradle, spring, spring-boot, spring-initializr, build-optimization, best-practices ]
date: 2024-03-31T00:00:00-06:00
---

## Introduction

You have an idea for a new microservice, so naturally you head to the [Spring Initializr](https://start.spring.io/) to generate a new Spring Boot project.
You decide to select Gradle, [the default build tool for the Spring Initializr since 2022](https://github.com/spring-io/start.spring.io/issues/1012), as your build tool of choice.
After all, Gradle [decreased the Spring Boot development team's CI build times by 3-4x and local build times by 20-30x](https://spring.io/blog/2020/06/08/migrating-spring-boot-s-build-to-gradle), so it's an obvious choice.

However, the Gradle build generated by the Spring Initializr is quite bare and doesn't leverage many of the exciting new features introduced in recent versions of Gradle.

In this blog post, we'll explore the best ways to improve the speed, reliability, and organization of the Gradle build for any Spring Boot project.
These also happen to be the exact changes I make after generating a new project for myself.

It's worth noting that while this blog post is written in the context of a freshly generated Spring Boot project, these would be great changes to make to any project using Gradle.

### 1. Use the Gradle Kotlin DSL

This is the only recommendation I will make that is best done _before_ you generate a new project.
The Gradle Kotlin DSL provides a type-safe way to configure your build and provides better integration with IDEs like IntelliJ IDEA than the traditional Groovy DSL.
If you've never used Kotlin before or are only familiar with the Groovy DSL, you have nothing to worry about &mdash;
the Kotlin DSL is easy to pick up and uses the same familiar Gradle APIs as the Groovy DSL.

If you're already using the Groovy DSL, you can migrate your build scripts to the Kotlin DSL, but it may or may not be trivial depending on how complex your build scripts are.
Fortunately, the Gradle documentation has [a guide outlining the Kotlin DSL migration process](https://docs.gradle.org/current/userguide/migrating_from_groovy_to_kotlin_dsl.html), and it can be done incrementally by migrating one build script at a time.
Ideally you choose `Gradle - Kotlin` as the project type on the [Spring Initializr](https://start.spring.io/#!type=gradle-project-kotlin) and start using the Kotlin DSL from the very beginning.

Here's a minimal example of a `build.gradle.kts`  for a Spring Boot project using the Kotlin DSL:

[//]: # (todo make these versions dynamic)
```kotlin
plugins {
    java
    id("org.springframework.boot") version "#{:springBootVersion}"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter:#{:springBootVersion}")
}
```

Kotlin is interoperable with Java and [fully compatible with Spring Boot](https://spring.io/guides/tutorials/spring-boot-kotlin), so it's also a great language for writing Spring Boot applications!

You can learn more about the Kotlin DSL in the Gradle documentation: https://docs.gradle.org/current/userguide/kotlin_dsl.html

### 2. Upgrade Gradle to the latest version

Projects generated using the Spring Initializr do not always come with the latest version of Gradle.
The first thing I do _after_ I generate a new project is upgrade the Gradle Wrapper to the latest version.
As an added layer of security, I also recommend [configuring distribution checksum verification](https://docs.gradle.org/current/userguide/gradle_wrapper.html#configuring_checksum_verification) which will fail the build if the configured checksum does not match the checksum for the Gradle distribution you're downloading.

As of Gradle 8.1, upgrading the Wrapper to the latest version is easy.
You can simply run the following command after generating your project:

```shell
./gradlew wrapper --gradle-version=latest --gradle-distribution-sha256-sum=#{:gradleDistributionSha256Sum}
```

Newly generated projects will be generated with a newer version than Gradle 8.1, but if you happen to already be on a lower version you'll have to specify the version when upgrading the Wrapper:

```shell
./gradlew wrapper --gradle-version=#{:gradleVersion} --gradle-distribution-sha256-sum=#{:gradleDistributionSha256Sum}
```

You can learn more about the Gradle Wrapper in the Gradle documentation: https://docs.gradle.org/current/userguide/gradle_wrapper.html

### 3. Publish Gradle build scans

A build scan is a web-based report that captures everything about your build like: the outcome of every task, test results, build performance metrics, dependencies, the console log, and more.
You should configure this as soon as possible since the deep insights on build failures and deprecations from a build scan will significantly help any time you encounter a problem with your build.

I recommend configuring Gradle to publish a build scan on every build invocation.
At the end of the build you'll see a unique link printed to the console that will take you to the build scan for that build. 

You can configure build scans for your build by adding the [Develocity Gradle plugin](https://docs.gradle.com/enterprise/gradle-plugin/#connecting_to_scans_gradle_com) to your `settings.gradle.kts` file.
I also recommend adding the [Common Custom User Data Gradle plugin](https://github.com/gradle/common-custom-user-data-gradle-plugin) which add additional metadata like the Git branch and commit hash to the published build scans.

Add the following to your `settings.gradle.kts` to configure build scans for your build:

```kotlin
plugins {
    id("com.gradle.enterprise") version "#{:develocityGradlePluginVersion}"
    id("com.gradle.common-custom-user-data-gradle-plugin") version "#{:commonCustomUserDataGradlePluginVersion}"
}

gradleEnterprise {
    buildScan {
        publishAlways()
        isUploadInBackground = !System.getenv().containsKey("CI")
        termsOfServiceAgree = "yes"
        termsOfServiceUrl = "https://gradle.com/terms-of-service"
    }
}
```

Gradle also offers a paid version of build scans called [Develocity](https://gradle.com/develocity/) that offers additional features suited for enterprises. 
In fact, Gradle partners with the Spring team (and other OSS projects) to provide them with a free instance of Develocity.
The Spring team's Develocity instance is visible publicly, and you can check it out for yourself at [ge.spring.io](https://ge.spring.io/).

You can learn more about build scans on Gradle's website: https://scans.gradle.com/

### 4. Enable build caching

When Gradle encounters a task that it has already executed it will not run it again if the output of that task already exists in the build directory.
This will result in the outcome of that task being `UP-TO-DATE`.
This is called [incremental build](https://docs.gradle.org/current/userguide/incremental_build.html).
Incremental build is enabled by default, and it can significantly improve the speed of your build.
The downside of incremental build is that it can only skip tasks whose inputs have not changed **since the most recent build invocation**.

Build caching however provides a persistent location for task results outside the build directory.
This means build caching can restore the output of any cacheable task that has been previously computed and **is not limited to the most recent build invocation**.
While mostly negligible, there is a time cost in storing and loading cache entries, meaning incremental build is still faster since it has no additional overhead

When you enable build caching, Gradle will store the outputs of all cacheable tasks in a directory on disk which is typically located at `.gradle/caches/build-cache-1`.
This is called the _local_ build cache.
You can also configure [a _remote_ build cache](https://docs.gradle.org/current/userguide/build_cache.html#sec:build_cache_configure_remote) which allows teams to share build cache entries across machines to speed up both local developer builds and CI builds.

You can enable build caching by invoking Gradle with the `--build-cache` command line option, but I recommend permanently enabling build caching by adding the following property to your `gradle.properties` file:

```properties
org.gradle.caching=true
```

You can learn more about build caching in the Gradle documentation: https://docs.gradle.org/current/userguide/build_cache.html

### 5. Enable parallel execution

When you have a build with multiple projects, the default behavior is to build them serially, that is one at a time, even if those projects are not dependent on one another.
When parallel execution is enabled, Gradle will build any non-dependant projects in parallel.
This is one of the easiest and most low effort ways to speed up your build.

You can enable parallel execution by invoking Gradle with the `--parallel` command line parameter, or enable it permanently by adding the following property to your `gradle.properties` file: 

```properties
org.gradle.parallel=true
```

You can learn more about parallel execution in the Gradle documentation: https://docs.gradle.org/current/userguide/performance.html#parallel_execution

### 6. Enable configuration cache

A Gradle build is broken up into three distinct phases: initialization, configuration, and execution.

The **initialization phase** bootstraps the build environment and typically only takes a few seconds at most.

The **configuration phase** evaluates the `build.gradle.kts` file of every project and constructs the task graph.
This phase can take quite some time depending on the complexity of the build.

The **execution phase** is when tasks are actually run. 
As we've seen, we can leverage incremental build and the build cache to speed up this phase.

The configuration cache is similar to the build cache, but will cache the result of the configuration phase such that we can skip the configuration phase of subsequent builds for the same set of requested tasks.
Any change to the build will invalidate the configuration cache, requiring the configuration phase to run again.

As an added bonus, the configuration cache brings [a finer-grained parallelism model](https://docs.gradle.org/current/userguide/configuration_cache.html#config_cache:intro:performance_improvements) than parallel execution.
Unlike parallel execution, enabling the configuration cache allows non-dependant tasks from the same project to run in parallel.

You can enable the configuration cache by invoking Gradle with the `--configuration-cache` command line parameter, or enable it permanently by adding the following property to your `gradle.properties` file:

```properties
org.gradle.configuration-cache=true
```

Until Gradle 8.1 the configuration cache was an experimental feature so if you're on an older version you will need to use a different property:

```properties
org.gradle.unsafe.configuration-cache=true
```

You can learn more about the configuration cache in the Gradle documentation: https://docs.gradle.org/current/userguide/configuration_cache.html

### 7. Replace `io.spring.dependency-management` with a Gradle platform

The primary use case for the Spring Dependency Management plugin is to provide support for importing Maven BOMs to align dependency versions.
Gradle has had the ability to import a Maven BOM since Gradle 5.0 released in 2018.
When you import a Maven BOM, you no longer have to explicitly declare versions for dependencies defined in the BOM as their versions are automatically aligned.
There are other features of the plugin such as the ability to reference properties defined in the BOM, but [this isn't supported when using the Kotlin DSL](https://docs.spring.io/dependency-management-plugin/docs/current/reference/html/#accessing-properties).

When you use the Spring Dependency Management plugin the Spring Boot plugin [will automatically import](https://docs.spring.io/spring-boot/docs/current/gradle-plugin/reference/htmlsingle/#managing-dependencies.dependency-management-plugin) the [`spring-boot-dependencies`](https://central.sonatype.com/artifact/org.springframework.boot/spring-boot-dependencies) BOM.
This is why you do not have to specify the version for many Spring and other miscellaneous dependencies.
To achieve the same result using Gradle's native BOM support you should remove the Spring Dependency Management plugin and replace it with a platform importing the `spring-boot-dependencies` BOM.
The Spring Boot plugin actually exposes a `BOM_COORDINATES` constant that can be used to declare the platform.
This will import the same version of the BOM as that of the Spring Boot Gradle plugin.

Here are the changes you need to make in order to replace the Spring Dependency Management plugin with Gradle's native BOM support:

```kotlin
import org.springframework.boot.gradle.plugin.SpringBootPlugin

plugins {
    java
    id("org.springframework.boot") version "#{:springBootVersion}"
    
    // Delete this - it's no longer necessary!
    //id("io.spring.dependency-management") version "#{:springDependencyManagementGradlePlugin}"
}

dependencies {
    implementation(platform(SpringBootPlugin.BOM_COORDINATES))
    
    // Look mom no versions!
    implementation("org.springframework.boot:spring-boot-starter")

    // If you're using a Spring Cloud dependency you can import its BOM too
    implementation(platform("org.springframework.cloud:spring-cloud-dependencies:#{:springCloudVersion}"))
}
```

You can learn more about platforms and BOM support in the Gradle documentation: https://docs.gradle.org/current/userguide/platforms.html#sub:bom_import