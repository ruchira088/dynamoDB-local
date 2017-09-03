package com.ruchij.utils

import java.util.concurrent

import scala.concurrent.{ExecutionContext, Future}

object ScalaUtils
{
  def toScalaFuture[A](javaFuture: concurrent.Future[A])(implicit executionContext: ExecutionContext): Future[A] =
    Future {
      javaFuture.get()
    }

  def toOption[A](value: A): Option[A] = value match
    {
      case null => None
      case _ => Some(value)
    }
}
