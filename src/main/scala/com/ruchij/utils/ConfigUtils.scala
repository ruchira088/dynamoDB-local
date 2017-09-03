package com.ruchij.utils

object ConfigUtils
{
  val STAGE_ENV_NAME = "STAGE"

  def getEnvValue(name: String): Option[String] = ScalaUtils.toOption(System getenv name)
}
