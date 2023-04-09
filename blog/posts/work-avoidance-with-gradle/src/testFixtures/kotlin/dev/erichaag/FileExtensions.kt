package dev.erichaag

import java.io.File

fun File.replaceText(regex: Regex, replacement: String) {
  writeText(readText().replace(regex, replacement))
}
