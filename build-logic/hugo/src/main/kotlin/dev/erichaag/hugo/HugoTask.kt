package dev.erichaag.hugo

import org.gradle.api.DefaultTask

abstract class HugoTask : DefaultTask() {

  init {
    group = "hugo"
  }
}
