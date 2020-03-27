package configurare

import com.typesafe.config.{Config, ConfigFactory}
import scala.jdk.CollectionConverters._

package object shim {
  implicit class TypesafeConfig (config: Config) {
    def shim(shims: Map[String, Any]): Config = {
      ConfigFactory.parseMap(shims.asJava).withFallback(config)
    }
  }
}
