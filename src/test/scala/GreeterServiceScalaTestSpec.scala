package example.myapp.helloworld

import akka.http.scaladsl.testkit.ScalatestRouteTest
import com.codacy.service.ServiceComponents
import hello.world.definitions.{HelloReply, HelloRequest}
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}

import scala.language.postfixOps

class GreeterServiceScalaTestSpec
  extends Matchers
    with WordSpecLike
    with BeforeAndAfterAll
    with ScalatestRouteTest {

  import hello.world.AkkaHttpImplicits._

  val serviceComponents = new ServiceComponents()

  "GreeterService" should {
    "reply to single get request" in {
      Get("/v2/say/hello?name=Alice") ~> serviceComponents.greeterRoutes ~> check {
        responseAs[HelloReply] shouldEqual HelloReply("Hello, Alice!")
      }
    }

    "reply to single post request" in {
      Post("/v2/say/hello", HelloRequest("Alice")) ~> serviceComponents.greeterRoutes ~> check {
        responseAs[HelloReply] shouldEqual HelloReply("Hello, Alice!")
      }
    }
  }

}
