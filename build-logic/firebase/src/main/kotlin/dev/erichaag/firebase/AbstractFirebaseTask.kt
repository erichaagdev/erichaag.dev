package dev.erichaag.firebase

import org.gradle.api.DefaultTask

abstract class AbstractFirebaseTask : DefaultTask() {

  init {
    group = "firebase"
  }
}
