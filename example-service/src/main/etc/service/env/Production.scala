
import java.util.UUID

import com.softwaremill.quicklens._
import configurare.shim._
import example.config.{Configuration, Default, EncryptedSecret, FarCache}

object Production extends Configuration {

  val service = Default.service
    .modify(_.clientId)         .setTo(UUID.fromString("6641d8da-c8ea-43a8-9dcd-4cb2753795cd"))
    .modify(_.clientSecret)     .setTo(EncryptedSecret("ZGMzMGJjYTEtMWVmNi00NGI3LWIyNmItOTliYTBmZjM0MmFlCg=="))
    .modify(_.cacheConfig.ttl)  .setTo(1000)
    .modify(_.cache)            .setTo(FarCache(
    host = "memcache.example.com",
    port = 11211,
    retries = 4
  ))
  val typesafeConfig = Default.typesafeConfig.shim ( Map(
    "env" -> "production"
  ))
}
