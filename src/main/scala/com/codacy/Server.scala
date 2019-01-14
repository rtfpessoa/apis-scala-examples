package example.myapp.helloworld

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.{ActorMaterializer, Materializer}
import com.codacy.service.ServiceComponents
import com.typesafe.config.ConfigFactory

import scala.concurrent.{ExecutionContext, Future}

object Server {

  def main(args: Array[String]): Unit = {
    val conf = ConfigFactory.defaultApplication()
    val system = ActorSystem("HelloWorld", conf)
    new Server(system).run()
    // ActorSystem threads will keep the app alive until `system.terminate()` is called
  }
}

class Server(system: ActorSystem) {

  // Akka boot up code
  implicit lazy val sys: ActorSystem = system
  implicit lazy val mat: Materializer = ActorMaterializer()
  implicit lazy val ec: ExecutionContext = sys.dispatcher

  val serviceComponents = new ServiceComponents()

  def run(): Future[Http.ServerBinding] = {
    // Bind service handler server to localhost:8080
    val bound = Http().bindAndHandleAsync(
      serviceComponents.greeterService,
      interface = "127.0.0.1",
      port = 8080)

    // report successful binding
    bound.foreach { binding =>
      println(s"Server bound to: ${binding.localAddress}")
    }

    bound
  }

}
