---
title: Work Avoidance — Speed Up Gradle Builds by Doing No Work
summary: In this article I explore the concept of work avoidance, a way to improve build speeds — by doing no work at all.
categories: [ Gradle ]
tags: [ build-optimization ]
date: 2023-01-15T11:35:02-06:00
draft: true
---

<!--more-->

## Introduction

Let's start exploring this topic by first looking at some common answers to the following question:

> How can I improve the speed of a Gradle build?

You may hear the following responses:

- Enable or improve build parallelization
- Update Gradle, Java, and plugin versions
- Purchase more powerful hardware

These are all valid answers to the question.
But, there is another answer:

> We can improve the speed of a Gradle build by avoiding work we've already done.

This concept is called **work avoidance** and is one of the best ways to improve Gradle build speeds.

## What is work avoidance?

The concept of work avoidance is simple.
Why should we repeat work we've already done?
Why should we spend time re-compiling code that has already been previously compiled?
Why re-run tests that we already know will pass?

Work avoidance is a key feature of Gradle and is one reason it is faster when compared to other build tools.

In order to better understand the concept of work avoidance, we must first understand how Gradle defines work.
In Gradle, a unit of work is defined as a **task**.
When executing a Gradle build, we are executing one or more tasks.
For example, in a typical Java build there are Gradle tasks to: compile code, create artifacts, and execute tests.

At a high level, there are three main components that make up a task in Gradle:

- One or more **inputs**
- The **action** to perform
- One or more **outputs**

These components are exactly the same as another construct developers use regularly, a _function_, and you can think of it that way!

So, how does Gradle know whether to execute a task or if the task can be avoided?

> Gradle can avoid executing a task when it has previously executed the task with the same inputs and already knows or has the result of executing its action, its outputs.

When this happens, there's no need to execute the task's action.
We can instead reuse the outputs from a previous execution of the task.

## Types of work avoidance in Gradle

As mentioned previously, work avoidance is a key feature in Gradle.
In fact, there are several forms of work avoidance:

- Incremental building
- Build caching
- Configuration caching

Let's explore each of these in detail.

### Incremental building

Incremental building is a form of work avoidance that skips running a task when a task's inputs or outputs have not changed, and the output is already present in the `build` directory. It is enabled by default.

Following a `clean`, or when running a build for the first time, you might see something like this:

```shell
{{% work-avoidance-with-gradle/incremental-building-output-1 %}}
```

In this execution, Gradle is reporting that out of the 6 actionable tasks, all 6 tasks were executed.
Additionally, the project directory now has a `build` directory containing the outputs of each task. 

So why does Gradle report that there were only 6 actionable tasks when the output shows 11?
An actionable task is a task that has an **action** defined.
Tasks that do not have an action are called [_lifecycle tasks_](https://docs.gradle.org/current/userguide/more_about_tasks.html#sec:lifecycle_tasks).
Lifecycle tasks do not need to perform any work and are not executed.

- The actionable tasks are: 
  - `compileJava`
  - `processResources`
  - `jar`
  - `compileTestJava`
  - `processTestResources`
  - `test`
- The lifecycle tasks are:
  - `classes`
  - `assemble`
  - `testClasses`
  - `check`
  - `build`

Given that no code changes have been made, then the next execution will look like this:

```shell
{{% work-avoidance-with-gradle/incremental-building-output-2 %}}
```

What's different about this execution?
We can see the outcome of every task was `UP-TO-DATE` and the build duration was significantly quicker.
Gradle was able to skip the execution of all 6 actionable tasks because it did not detect changes to any task inputs or outputs.
The `build` directory still contains the outputs of each task from the first build.

However, very rarely do we execute two builds back-to-back without making any changes.
What happens when we do make a change to one of the task inputs?
Imagine we make a change to a source file in the main source set.

```shell
{{% work-avoidance-with-gradle/incremental-building-output-3 %}}
```

This time only 3 tasks were executed while the other 3 remained up-to-date.

- The executed actionable tasks are:
  - `compileJava`
  - `jar`
  - `test`
- The avoided actionable tasks are:
  - `processResources`
  - `compileTestJava`
  - `processTestResources`

Gradle re-executes `compileJava` because there was a change to one of its inputs, a Java source file.
This causes the `jar` and `test` tasks to re-execute because their inputs have now changed as a result of recompiling the code.

However, Gradle was able to skip the execution of the other tasks because none of their inputs or outputs changed.
Instead, we are able to leverage the work done previously and reuse the outputs already present in the `build` directory.

### Build caching

Build caching takes this a step further by storing, or _caching_, a task's outputs for later retrieval.
This is different from incremental building in that build caching stores a task's outputs somewhere outside the project directory.
This location is called the **build cache**.

The main benefit that build caching has over incremental building is that it is persistent.
We can run `clean` or delete and re-clone a project and still benefit from build caching.

When we execute a task and its outputs are not present in the `build` directory, then Gradle will check to see if they are present within the build cache.
If they are, then Gradle will fetch and restore the task's outputs to the `build` directory and skip the execution of the task. This is called a **cache hit**.

The overhead introduced by fetching and restoring a task's outputs from the build cache means that not all tasks will benefit from build caching.
Therefore, task authors must explicitly designate a task as cacheable.
These tasks are called **cacheable tasks**.

There are two different types of build caching: **local** and **remote**.
Let's explore them both.

#### Local build caching

Local build caching uses the local filesystem as a build cache.
Build caching is not enabled by default.
To enable build caching, the following property can be added to a project's `gradle.properties` file.

```properties
{{% work-avoidance-with-gradle/local-build-caching-gradle-properties %}}
```

When build caching is enabled, Gradle will additionally store the outputs of all cacheable tasks inside the Gradle user home directory.
Now, following a `clean` or 

```shell
{{% work-avoidance-with-gradle/local-build-caching-output-1 %}}
```

```shell
{{% work-avoidance-with-gradle/local-build-caching-output-2 %}}
```

#### Remote build caching

wip

### Configuration caching

wip
