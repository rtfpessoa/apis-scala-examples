package com.codacy.service

import akka.actor.ActorSystem
import akka.http.scaladsl.model.{HttpRequest, HttpResponse}
import akka.stream.Materializer
import com.helloworld.GreeterServiceHandler
import example.myapp.helloworld.GreeterServiceImpl

import scala.concurrent.{ExecutionContext, Future}

trait ServiceComponents {

  implicit def sys: ActorSystem

  implicit def mat: Materializer

  implicit def ec: ExecutionContext

  val greeterService: HttpRequest => Future[HttpResponse] =
    GreeterServiceHandler(new GreeterServiceImpl(mat))

}
