package com.ruchij

import akka.actor.ActorSystem
import com.amazonaws.auth.{AWSCredentials, AWSStaticCredentialsProvider}
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration
import com.amazonaws.services.dynamodbv2.{AmazonDynamoDBAsync, AmazonDynamoDBAsyncClientBuilder}
import com.ruchij.utils.{AwsUtils, DynamoDbUtils}

import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContext}

object LocalDynamoDbApp extends App
{
  implicit val asyncDbClient: AmazonDynamoDBAsync =
    AmazonDynamoDBAsyncClientBuilder.standard()
      .withCredentials(AwsUtils.getCredentialProvider())
      .withEndpointConfiguration(new EndpointConfiguration("http://dynamodb-local:8000", "local"))
      .build()

  implicit val actorSystem: ActorSystem = ActorSystem("local-dynamoDB-app")

  implicit val executionContext: ExecutionContext = actorSystem.dispatcher

  val tableDescription = Await.result(DynamoDbUtils.createTable("example-table"), 60 seconds)

  println(tableDescription)
}
