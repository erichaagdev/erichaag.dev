package dev.erichaag.hugo

import dev.erichaag.hugo.HugoPlugin.Companion.HUGO_GROUP_NAME
import org.gradle.api.DefaultTask

abstract class AbstractHugoTask : DefaultTask() {

  init {
    group = HUGO_GROUP_NAME
  }
}
