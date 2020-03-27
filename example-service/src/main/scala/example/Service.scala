package example
import configurare.ConfigLoader
import example.config.{Configuration, Default}

object Service extends App {

  //use the default configuration root ('.') , or the one provided by system property 'scala.config.root'
  ConfigLoader.initRoot()

  //use the default configuration  or the one provided by system property 'scala.config'
  val config = ConfigLoader.get[Configuration]().getOrElse (Default)

  /*
  // Can lookup any configuration included in the configuration root
  val devConfig = ConfigLoader.get[Configuration]("Development").get
  val prodConfig = ConfigLoader.get[Configuration]("Production").get
  */

  // Can also lookup nested properties
  val port = ConfigLoader.get[Int]("Production.service.port").get

  val service = {
    import akka.actor.ActorSystem
    import akka.http.scaladsl.Http
    import akka.http.scaladsl.server.Directives._
    import akka.stream.ActorMaterializer
    import scala.io.StdIn

    implicit val system = ActorSystem("Service", Some(config.typesafeConfig), None, None)
    implicit val materializer = ActorMaterializer()
    implicit val executionContext = system.dispatcher

    val route =
      path("hello") {
        get {
          complete("Hello, world")
        }
      }

    Http().bindAndHandle(route, config.service.host, port)

    println(s"Service started: CTRL-C to stop...")
  }
}
