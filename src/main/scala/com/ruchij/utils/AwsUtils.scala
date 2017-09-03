package com.ruchij.utils

import com.amazonaws.auth._

object AwsUtils
{
  private def emptyCredentials: AWSCredentials = new BasicAWSCredentials("", "")

  def getCredentialProvider(): AWSCredentialsProvider = ConfigUtils.getEnvValue(ConfigUtils.STAGE_ENV_NAME) match
    {
      case Some("PRODUCTION") => new EnvironmentVariableCredentialsProvider()
      case _ => new AWSStaticCredentialsProvider(emptyCredentials)
    }

}
