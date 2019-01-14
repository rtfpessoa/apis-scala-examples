package com.codacy.service

import akka.actor.ActorSystem
import akka.http.scaladsl.model.{HttpRequest, HttpResponse}
import akka.http.scaladsl.server.Route
import akka.stream.Materializer
import example.myapp.helloworld.GreeterServiceImpl
import hello.world.sync.SyncResource

import scala.concurrent.{ExecutionContext, Future}

class ServiceComponents(implicit
                        system: ActorSystem,
                        materializer: Materializer,
                        executionContext: ExecutionContext) {

  val greeterRoutes: Route = SyncResource.routes(new GreeterServiceImpl())

  val greeterService: HttpRequest => Future[HttpResponse] = Route.asyncHandler(greeterRoutes)

}
