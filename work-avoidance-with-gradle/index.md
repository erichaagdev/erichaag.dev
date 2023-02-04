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

These are all valid answers to the question. But, there is another answer:

> We can improve the speed of a Gradle build by avoiding work we've already done.

This concept is called **work avoidance** and is one of the best ways to improve Gradle build speeds.

## What is work avoidance?

The concept of work avoidance is simple.
Why should we repeat work we've already done?
Let's start by applying the concept of work avoidance to a hypothetical scenario.

Imagine you are starting your own residential construction business.
You've been approached by a potential client to construct a 6' x 8' shed for their backyard.
You spend the next few hours drafting blueprints according to their exact specifications.
The next day, you present the blueprints to the client.
They like what they see and want to hire you for the job!

The following week, you are approached by a new potential client.
They've heard of your great work and would also like to hire you to construct a 6' x 8' shed.
This time there is no need to spend hours drafting blueprints.
Since the criteria is the same, you can reuse the blueprints you drafted last week.
You are able to present the blueprints to your client immediately.
Another satisfied client!

## Work avoidance in Gradle

So, what does the concept of work avoidance mean in the context of Gradle?
The idea is exactly the same.
Why should we spend time re-compiling code that has already been previously compiled?
Why re-run tests that we already know will pass?

Work avoidance is a core concept in Gradle and is one reason it is faster when compared to other build tools.

In order to better understand the concept of work avoidance, we must first understand how Gradle defines work.
In Gradle, a unit of work is defined as a **task**.
When executing a Gradle build, we are executing one or more tasks.
For example, there are Gradle tasks to: compile Java code, create code artifacts, and execute tests.

At a high level, there are three main components that make up a task in Gradle:

- One or more **inputs**
- The **action** to perform
- One or more **outputs**

You may notice the components of a task are exactly the same as another construct developers use regularly, a _function_!

So, how does Gradle know whether to execute a task or if the task can be avoided?

> Gradle can avoid executing a task when it has previously executed the task with the same inputs and already has or knows the result of executing its action, its outputs.

When this happens, there's no need to execute the task's action.
We can instead reuse the outputs from a previous execution of the task.

## Types of work avoidance in Gradle

As mentioned previously, the concept of work avoidance is a core concept in Gradle.
In fact, there are several forms of work avoidance:

- Incremental building
- Build caching
- Configuration caching

Let's explore each of these in detail.

### Incremental building

wip

### Build caching

wip

#### Local build caching

wip

#### Remote build caching

wip

### Configuration caching

wip

{{< work-avoidance-with-gradle/foo >}}
