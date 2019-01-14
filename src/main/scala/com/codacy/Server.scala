package example.myapp.helloworld

import akka.actor.ActorSystem
import akka.http.scaladsl.UseHttp2.Always
import akka.http.scaladsl.{Http, HttpConnectionContext}
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

class Server(system: ActorSystem) extends ServiceComponents {

  // Akka boot up code
  implicit lazy val sys: ActorSystem = system
  implicit lazy val mat: Materializer = ActorMaterializer()
  implicit lazy val ec: ExecutionContext = sys.dispatcher

  def run(): Future[Http.ServerBinding] = {
    // Bind service handler server to localhost:8080
    val bound = Http().bindAndHandleAsync(
      greeterService,
      interface = "127.0.0.1",
      port = 8080,
      connectionContext = HttpConnectionContext(http2 = Always))

    // report successful binding
    bound.foreach { binding =>
      println(s"gRPC server bound to: ${binding.localAddress}")
    }

    bound
  }

}
