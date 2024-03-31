package dev.erichaag

import org.intellij.lang.annotations.Language
import java.io.File

class ProjectFile(private val file: File) {

  fun replace(@Language("RegExp") regex: String, contents: String): String {
    return contents.also { file.writeText(file.readText().replace(regex.toRegex(), it)) }
  }

  infix fun append(contents: String): String {
    return contents.also { file.appendText("$it\n") }
  }

  infix fun write(contents: String): String {
    if (!file.exists()) {
      file.parentFile.mkdirs()
    }
    return contents.trimIndent().also { file.writeText("$it\n") }
  }
}
