
import java.util.UUID

import com.softwaremill.quicklens._
import configurare.shim._
import example.config.{Configuration, Default, EncryptedSecret, FarCache}

object Development extends Configuration {
  val service = Default.service
    .modify(_.clientId)         .setTo(UUID.fromString("3155f4f9-d327-4ea9-b6e9-690567329547"))
    .modify(_.clientSecret)     .setTo(EncryptedSecret("YzQ2MmE2MzUtZDM1Zi00YzdlLWE5ZGUtYjEyY2E3YTk4MDdjCg=="))
    .modify(_.cacheConfig.ttl)  .setTo(1000)
    .modify(_.cache)            .setTo(FarCache(
        host = "memcache.example.com",
        port = 11211,
        retries = 4
    ))
   val typesafeConfig = Default.typesafeConfig.shim ( Map(
      "env" -> "development"
  ))
}
