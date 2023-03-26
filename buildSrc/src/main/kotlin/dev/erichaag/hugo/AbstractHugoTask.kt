package dev.erichaag.hugo

import org.gradle.api.DefaultTask

abstract class AbstractHugoTask : DefaultTask() {

  init {
    group = "hugo"
  }
}
