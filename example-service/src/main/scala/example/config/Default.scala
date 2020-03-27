package example.config

import java.util.UUID

import com.typesafe.config.{Config, ConfigFactory}
import configurare.shim._

object Default extends Configuration {
  val service = ServiceConfig(
    name = "Default Service Name",
    host = "localhost",
    port = 8181,
    clientId = UUID.fromString("81b72ebf-a4c6-40fe-8516-a10744a70037"),
    clientSecret = EncryptedSecret("Y2I5YTlmZDItMTY1Ni00ZjkxLThlZmQtMDk5ZGU3ZTliNjRhCg=="),
    cacheConfig = CacheConfig(namespace = "default.service", ttl = 3600),
    cache = NearCache
  )

  override val typesafeConfig: Config = ConfigFactory.load().shim(Map(
    "akka.loglevel" -> "WARNING",
    "akka.ssl-config.loose.disableHostnameVerification" -> false,
    "akka.ssl-config.acceptAnyCertification" -> false,
    "akka.http.default-http-port" -> 8181
  ))

}
