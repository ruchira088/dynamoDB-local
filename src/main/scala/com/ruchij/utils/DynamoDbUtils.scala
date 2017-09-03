package com.ruchij.utils

import akka.actor.ActorSystem
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBAsync
import com.amazonaws.services.dynamodbv2.model._

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future, Promise}

object DynamoDbUtils
{
  def createTable(tableName: String)
        (implicit amazonDynamoDBAsync: AmazonDynamoDBAsync, actorSystem: ActorSystem, executionContext: ExecutionContext): Future[TableDescription] =
  {
    val createTableRequest = new CreateTableRequest()
      .withTableName(tableName)
      .withKeySchema(
        new KeySchemaElement().withAttributeName("id").withKeyType(KeyType.HASH),
        new KeySchemaElement().withAttributeName("username").withKeyType(KeyType.RANGE)
      )
      .withAttributeDefinitions(
        new AttributeDefinition().withAttributeName("id").withAttributeType(ScalarAttributeType.S),
        new AttributeDefinition().withAttributeName("username").withAttributeType(ScalarAttributeType.S)
      )
      .withProvisionedThroughput(
        new ProvisionedThroughput()
          .withReadCapacityUnits(5L)
          .withWriteCapacityUnits(5L)
      )

    for {
      _ <- ScalaUtils.toScalaFuture(amazonDynamoDBAsync.createTableAsync(createTableRequest))
      result <- resolveWhenTableActive(tableName)
    } yield result
  }

  def resolveWhenTableActive(tableName: String)
        (implicit amazonDynamoDBAsync: AmazonDynamoDBAsync, actorSystem: ActorSystem, executionContext: ExecutionContext): Future[TableDescription] =
  {

    for {
      tableDescription <- ScalaUtils.toScalaFuture(amazonDynamoDBAsync.describeTableAsync(tableName))
      tableStatus = tableDescription.getTable.getTableStatus
      result <- {
        println(s"$tableName status: $tableStatus")

        if (tableStatus == "ACTIVE")
          Future.successful(tableDescription.getTable)
        else {
          val promise = Promise[TableDescription]()

          actorSystem.scheduler.scheduleOnce(3 seconds) {
            promise.completeWith(resolveWhenTableActive(tableName))
          }

          promise.future
        }
      }
    } yield result


  }
}
