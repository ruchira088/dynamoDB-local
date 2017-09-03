package com.ruchij.utils

import java.util.concurrent

import scala.concurrent.{ExecutionContext, Future}

object ScalaUtils
{
  def toScalaFuture[A](javaFuture: concurrent.Future[A])(implicit executionContext: ExecutionContext): Future[A] =
    Future {
      javaFuture.get()
    }
}
