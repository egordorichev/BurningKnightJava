package org.rexcellentgames.burningknight.util

object Utils {
  @JvmStatic
  fun pascalCaseToSnakeCase(string: String): String {
    val regex = "([a-z])([A-Z]+)"
    val replacement = "$1_$2"
    
    return string.replace(regex.toRegex(), replacement).toLowerCase()
  }
}