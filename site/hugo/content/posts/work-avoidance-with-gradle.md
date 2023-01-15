---
title: Work Avoidance — Speed Up Gradle Builds by Doing No Work
description: In this article I explore the concept of work avoidance, a way to improve build speeds — by doing no work at all.
categories: [ Gradle ]
tags: [ build-optimization ]
date: 2023-01-15T11:35:02-06:00
---

<!--more-->

## What is work avoidance?

The concept of work avoidance is simple. Why should we repeat work we've already done? Let's start by applying the concept of work avoidance to a real world hypothetical scenario.

Imagine you are starting your own residential construction business. You've been approached by a potential client to construct a 6' x 8' shed for their backyard. You spend the next few hours drafting blueprints according to their exact specifications. The next day, you present the blueprints to the client. They like what they see and want to hire you for the job!

The following week, you are approached by a new potential client. They've heard of your great work and would also like to hire you to construct a 6' x 8' shed. This time there is no need to spend hours drafting blueprints. Since the criteria is the same, you can reuse the blueprints you drafted last week. You are able to present the blueprints to your client immediately. Another satisfied client!

So, what does the concept of work avoidance mean in the context of Gradle? The idea is exactly the same. Why spend time re-compiling code that has  already been previously compiled? Why re-run tests we already know will pass?

Work avoidance is a core concept in Gradle and is one reason it is faster when compared to other build tools.

## Types of work avoidance in Gradle

- Incremental building
- Build caching
- Configuration caching

### Incremental building

wip

### Local build caching

wip

### Remote build caching

wip

### Configuration caching

wip
