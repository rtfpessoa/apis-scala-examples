package example.myapp.helloworld

import hello.world.definitions.{HelloReply, HelloRequest}
import hello.world.sync.{SyncHandler, SyncResource}

import scala.concurrent.Future

class GreeterServiceImpl extends SyncHandler {

  def getSayHello(respond: SyncResource.getSayHelloResponse.type)(name: String): Future[SyncResource.getSayHelloResponse] = {
    println(s"sayHello to ${name}")
    Future.successful(respond(HelloReply(s"Hello, ${name}!")))
  }

  def postSayHello(respond: SyncResource.postSayHelloResponse.type)(body: HelloRequest): Future[SyncResource.postSayHelloResponse] = {
    println(s"sayHello to ${body.name}")
    Future.successful(respond(HelloReply(s"Hello, ${body.name}!")))
  }

}
