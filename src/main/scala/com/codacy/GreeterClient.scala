package example.myapp.helloworld

import akka.actor.ActorSystem
import akka.http.scaladsl.model.HttpResponse
import akka.stream.ActorMaterializer
import com.codacy.service.ServiceComponents
import hello.world.definitions.{HelloReply, HelloRequest}
import hello.world.sync.SyncClient

import scala.concurrent.Future
import scala.concurrent.duration._
import scala.util.{Failure, Success}

object GreeterClient {

  def main(args: Array[String]): Unit = {
    // Boot akka
    implicit val sys = ActorSystem("HelloWorldClient")
    implicit val mat = ActorMaterializer()
    implicit val ec = sys.dispatcher

    val serviceComponents = new ServiceComponents()

    // Take details how to connect to the service from the config.
    val client = SyncClient.httpClient(serviceComponents.greeterService, host = "localhost")

    // Run examples for each of the exposed service methods.
    getSayHelloExample()
    postSayHelloExample()

    sys.scheduler.schedule(1.second, 1.second) {
      getSayHelloExample()
    }

    def getSayHelloExample(): Unit = {
      val reply = client.getSayHello("Alice")
      handleReply(reply.value)
    }

    def postSayHelloExample(): Unit = {
      val reply = client.postSayHello(HelloRequest("Alice"))
      handleReply(reply.value)
    }

    def handleReply(reply: Future[Either[Either[Throwable, HttpResponse], HelloReply]]) = {
      reply.onComplete {
        case Success(Right(message)) =>
          println(s"Greeting: $message")
        case Success(Left(Right(response))) =>
          println(s"Bad request: $response")
        case Success(Left(Left(error))) =>
          println(s"Problem with the request: ${error.getMessage}")
        case Failure(error) =>
          println(s"Could not make request: ${error.getMessage}")
      }
    }

  }

}
