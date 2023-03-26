import dev.erichaag.hugo.HugoExtension

plugins {
  id("dev.erichaag.hugo-base")
}

val hugo = extensions.create<HugoExtension>("hugo")
