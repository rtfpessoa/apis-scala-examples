import akka.actor.ActorSystem
import akka.http.scaladsl.testkit.ScalatestRouteTest
import com.codacy.service.ServiceComponents
import com.typesafe.config.ConfigFactory
import example.myapp.helloworld.Server
import hello.world.definitions.{HelloReply, HelloRequest}
import hello.world.sync.SyncClient
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{BeforeAndAfterAll, EitherValues, Matchers, WordSpecLike}

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.language.postfixOps

class GreeterClientSpec
  extends Matchers
    with WordSpecLike
    with ScalaFutures
    with EitherValues
    with BeforeAndAfterAll
    with ScalatestRouteTest {

  val serviceComponents = new ServiceComponents()

  lazy val serverSystem: ActorSystem = {
    val conf = ConfigFactory.defaultApplication().resolve()
    val sys = ActorSystem("GreeterServer", conf)
    val bound = new Server(sys).run()
    // make sure server is bound before using client
    bound.futureValue

    sys
  }

  override def afterAll: Unit = {
    Await.ready(serverSystem.terminate(), 5.seconds)
  }

  val client = SyncClient.httpClient(serviceComponents.greeterService, host = "http://localhost:8080")

  "GreeterClient" should {
    "reply to single get request" in {
      val reply = client.getSayHello("Alice")
      reply.value.futureValue.right.value should ===(HelloReply("Hello, Alice!"))
    }

    "reply to single post request" in {
      val reply = client.postSayHello(HelloRequest("Alice"))
      reply.value.futureValue.right.value should ===(HelloReply("Hello, Alice!"))
    }
  }

}
