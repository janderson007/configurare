package example.config

import java.util.UUID


trait Configuration {
  val service: ServiceConfig
  // Some external libraries expect this...
  val typesafeConfig: com.typesafe.config.Config
}


case class ServiceConfig(
                          name: String,
                          host: String,
                          port: Int,
                          clientId: UUID,
                          clientSecret: EncryptedSecret,
                          cacheConfig: CacheConfig,
                          cache: Cache
                        )

sealed trait Cache

case class CacheConfig(namespace: String, ttl: Long)

case class FarCache(host: String, port: Int, retries: Int) extends Cache

case object NearCache extends Cache

case class EncryptedSecret(encryptedValue: String) {

  // obviously not really encrypted, just for demonstration
  def decrypted: String = {
    new String(java.util.Base64.getDecoder.decode(encryptedValue.getBytes()))
  }
}