---
title: Work Avoidance — Speed Up Gradle Builds by Doing No Work
description: In this article I explore the concept of work avoidance, a way to improve build speeds — by doing no work at all.
categories: [ Gradle ]
tags: [ build-optimization ]
date: 2023-01-15T11:35:02-06:00
---

<!--more-->

## Introduction

Let's start exploring this topic by first looking at some common answers to the following question:

> How can I improve the speed of a Gradle build?

You may hear the following responses:

- Purchase more powerful hardware
- Update Gradle, Java, and plugin versions
- Enable or improve build parallelization

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

You may notice these components are exactly the same as another construct developers use regularly, a _function_!

So, how does Gradle know whether to execute a task or if the task can be avoided?

> Gradle can avoid executing a task when it has previously executed the task with the same inputs and already has or knows the result of executing its action, its outputs.

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
{{% work-avoidance-with-gradle/incremental-building-example-output-1 %}}
```

In this execution, Gradle is reporting that out of the 6 actionable tasks, all 6 tasks were executed.
Additionally, the project directory now has a `build` directory containing the outputs of each task. 

Why does Gradle report that there were only 6 actionable tasks when the output shows 11?
An actionable task is a task which has an **action** defined.
Tasks which do not have an action are called [_lifecycle tasks_](https://docs.gradle.org/current/userguide/more_about_tasks.html#sec:lifecycle_tasks).
Lifecycle tasks do not need to perform any work and are not executed.

Given that no code changes have been made, then the next execution will look like:

```shell
{{% work-avoidance-with-gradle/incremental-building-example-output-2 %}}
```

What's different about this execution?
We can see the outcome of every task was `UP-TO-DATE` and the build duration was significantly quicker.
Gradle was able to skip the execution of all 6 actionable tasks because it did not detect changes to any task inputs or outputs.
The `build` directory still contains the outputs of each task.

However, very rarely do we execute two builds back-to-back without making any changes.
What happens when we do make a change to one of the task inputs?
Imagine we make a change to a source file in the main source set.

```shell
{{% work-avoidance-with-gradle/incremental-building-example-output-3 %}}
```

This time only 3 tasks were executed while the other 3 remained up-to-date.
- The executed actionable tasks are: `compileJava`, `jar`, and `test`
- The avoided actionable tasks are: `processResources`, `compileTestJava`, and `processTestResources`

Gradle re-executes `compileJava` because there was a change to one of its inputs, a Java source file.
This causes the `jar` and `test` tasks to re-execute because their inputs have now changed as a result of recompiling the code.

However, Gradle was able to skip the execution of the other tasks because none of their inputs or outputs changed.
Instead, we are able to leverage the work done previously and reuse the outputs present in the `build` directory.

### Build caching

wip

#### Local build caching

```properties
{{% work-avoidance-with-gradle/local-build-example-gradle-properties %}}
```

```shell
{{% work-avoidance-with-gradle/local-build-example-output-1 %}}
```

```shell
{{% work-avoidance-with-gradle/local-build-example-output-2 %}}
```

```shell
{{% work-avoidance-with-gradle/local-build-example-output-3 %}}
```

#### Remote build caching

wip

### Configuration caching

wip
