/*
 * Copyright (C) 2018-2019 Lightbend Inc. <https://www.lightbend.com>
 */

package example.myapp.helloworld

import akka.actor.ActorSystem
import akka.grpc.GrpcClientSettings
import akka.stream.ActorMaterializer
import com.helloworld.{GreeterServiceClient, HelloReply, HelloRequest}
import com.typesafe.config.ConfigFactory
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.time.Span
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.language.postfixOps

class GreeterClientSpec
  extends Matchers
    with WordSpecLike
    with BeforeAndAfterAll
    with ScalaFutures {

  implicit val patience = PatienceConfig(5.seconds, Span(100, org.scalatest.time.Millis))

  implicit val serverSystem: ActorSystem = {
    val conf = ConfigFactory.defaultApplication().resolve()
    val sys = ActorSystem("GreeterServer", conf)
    val bound = new Server(sys).run()
    // make sure server is bound before using client
    bound.futureValue
    sys
  }

  val clientSystem = ActorSystem("GreeterClient")

  val client = {
    implicit val mat = ActorMaterializer.create(clientSystem)
    implicit val ec = clientSystem.dispatcher
    GreeterServiceClient(
      GrpcClientSettings.connectToServiceAt("127.0.0.1", 8080)
        .withTls(false))
  }

  override def afterAll: Unit = {
    Await.ready(clientSystem.terminate(), 5.seconds)
    Await.ready(serverSystem.terminate(), 5.seconds)
  }

  "GreeterService" should {
    "reply to single request" in {
      val reply = client.sayHello(HelloRequest("Alice"))
      reply.futureValue should ===(HelloReply("Hello, Alice"))
    }
  }

}
